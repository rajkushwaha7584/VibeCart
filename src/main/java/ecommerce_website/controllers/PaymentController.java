package ecommerce_website.controllers;

import ecommerce_website.model.*;
import ecommerce_website.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @PostMapping("/process")
    public String processPayment(
            @RequestParam("productIds") List<Long> productIds,
            @RequestParam("quantities") List<Integer> quantities,
            @RequestParam("email") String email,
            @RequestParam("flat") String flat,
            @RequestParam("building") String building,
            @RequestParam("sector") String sector,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("pincode") String pincode,
            Model model,
            HttpSession session
    ) {
        // ✅ 1. Get user from email
        User user = userRepo.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "User not found for email: " + email);
            return "error"; // error.html page
        }

        // ✅ 2. Build full address
        String fullAddress = String.format("%s, %s, %s, %s, %s - %s",
                flat, building, sector, city, state, pincode);

        // ✅ 3. Create and save Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryAddress(fullAddress);
        order.setOrderStatus("PLACED"); // or Pending
        order.setPaymentMethod("COD");
        order = orderRepo.save(order);

        // ✅ 4. Add items to order
        double grandTotal = 0.0;
        for (int i = 0; i < productIds.size(); i++) {
            Product product = productRepo.findById(productIds.get(i)).orElse(null);
            if (product != null) {
                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setOrder(order);
                item.setQuantity(quantities.get(i));
                item.setPrice(product.getPrice());
                double itemTotal = product.getPrice() * quantities.get(i);
                item.setTotalPrice(itemTotal);
                grandTotal += itemTotal;
                orderItemRepo.save(item);
            } else {
                System.err.println("Product not found: ID = " + productIds.get(i));
            }
        }

        // ✅ 5. Add totalAmount and order info to model

        model.addAttribute("totalAmount", grandTotal);
        model.addAttribute("email", email);
        model.addAttribute("address", fullAddress);
        return "payment"; // ✅ Renders Razorpay payment.html

    }

    // ✅ Optional: For online payment confirmation (Razorpay, etc.)
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, Model model) {
        model.addAttribute("paymentId", paymentId);
        return "payment_success";
    }
    @PostMapping("/cod")
    public String handleCodPayment(@RequestParam("address") String address, Principal principal, Model model) {
        try {
            // existing logic...
            return "thankyou";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Something went wrong while processing your order.");
            return "error";
        }
    }

}

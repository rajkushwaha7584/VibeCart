package ecommerce_website.controllers;

import ecommerce_website.model.*;
import ecommerce_website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PaymentController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @PostMapping("/payment")
    public String processPayment(
            @RequestParam("productIds") List<Long> productIds,
            @RequestParam("quantities") List<Integer> quantities,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            Model model) {

        User user = userRepo.findByEmail(email);

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("PLACED"); // optional
        order.setPaymentMethod("COD");  // optional
        orderRepo.save(order);

        for (int i = 0; i < productIds.size(); i++) {
            Product product = productRepo.findById(productIds.get(i)).orElse(null);
            if (product != null) {
                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setOrder(order);
                item.setQuantity(quantities.get(i));
                item.setPrice(product.getPrice());
                item.setTotalPrice(product.getPrice() * quantities.get(i));
                orderItemRepo.save(item);
            }
        }

        model.addAttribute("order", order);
        return "payment_success";
    }
}

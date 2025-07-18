package ecommerce_website.controllers;

import ecommerce_website.model.Order;
import ecommerce_website.model.User;
import ecommerce_website.repository.OrderRepository;
import ecommerce_website.repository.UserRepository;
import ecommerce_website.service.OrderService;
import ecommerce_website.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    /**
     * ✅ Show delivery form after cart selection
     */
    @PostMapping("/checkout")
    public String checkoutPage(@RequestParam List<Long> cartItemIds, Model model) {
        model.addAttribute("cartItemIds", cartItemIds);
        return "checkout"; // Page to enter address & confirm
    }

    /**
     * ✅ Place order after address submission
     */
    @PostMapping("/place")
    public String placeOrder(@RequestParam("cartItemIds") List<Long> cartItemIds,
                             @RequestParam("address") String address,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {

        User user = userService.findUserByEmail(userDetails.getUsername());

        // Place order with selected items & address
        Order order = orderService.placeOrder(user, cartItemIds, address);

        model.addAttribute("order", order);
        return "order_success"; // Show placed order summary
    }

    /**
     * ✅ View logged-in user's order history
     */
    @GetMapping("/history")
    public String viewOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        return "order_history"; // List of previous orders
    }
    
    @GetMapping("/order/success")
    public String showOrderSuccess(@RequestParam("id") Long orderId, Model model) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return "redirect:/";
        }
        model.addAttribute("order", order);
        return "order_success";
    }

}

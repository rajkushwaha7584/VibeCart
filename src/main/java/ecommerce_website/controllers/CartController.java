package ecommerce_website.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ecommerce_website.model.CartItem;
import ecommerce_website.model.Product;
import ecommerce_website.model.User;
import ecommerce_website.repository.CartItemRepository;
import ecommerce_website.repository.ProductRepository;
import ecommerce_website.repository.UserRepository;
import ecommerce_website.service.CartService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    private CartService cartService;
    
    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return "redirect:/error";
        }

        CartItem existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setProductName(product.getName());
            cartItem.setProductPrice(product.getPrice());
            cartItem.setImageUrl(product.getImageUrl()); // ✅ You were missing this
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);
        }

        return "redirect:/cart";
    }


//    @GetMapping("/cart")
//    public String showCart(Model model, Principal principal) {
//        User user = userRepository.findByEmail(principal.getName());
//        List<CartItem> cartItems = cartItemRepository.findByUser(user);
//
//        double total = cartItems.stream()
//                .mapToDouble(CartItem::getTotalPrice)
//                .sum();
//
//        model.addAttribute("cartItems", cartItems);
//        model.addAttribute("total", total);
//
//        return "cart";
//    }   
    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        model.addAttribute("cartItems", cartItems);

        return "cart"; // should map to `cart.html`
    }

    @PostMapping("/cart/update/{id}")
    public String updateQuantity(@PathVariable Long id, @RequestParam("quantity") int quantity, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        CartItem item = cartItemRepository.findByUserAndProduct_Id(user, id);
        if (item != null && quantity > 0) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        CartItem item = cartItemRepository.findByUserAndProduct_Id(user, id);
        if (item != null) {
            cartItemRepository.delete(item);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam(value = "selectedProductIds", required = false) List<Long> selectedIds,
                           Principal principal, Model model) {
        if (selectedIds == null || selectedIds.isEmpty()) {
            model.addAttribute("error", "Please select at least one product to checkout.");
            return "redirect:/cart";
        }

        User user = userRepository.findByEmail(principal.getName());

        List<CartItem> selectedItems = cartItemRepository.findByUserAndProduct_IdIn(user, selectedIds);
        double totalAmount = selectedItems.stream().mapToDouble(CartItem::getTotalPrice).sum();

        // Proceed with order logic or redirection to payment
        model.addAttribute("checkoutItems", selectedItems);
        model.addAttribute("totalAmount", totalAmount);
        return "checkout"; // Create a checkout.html
    }


}

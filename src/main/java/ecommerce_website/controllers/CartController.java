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

    // ✅ Add product to cart
    @GetMapping("/cart/go-add/{productId}")
    public String addToCart(@PathVariable Long productId, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return "redirect:/error";

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
            cartItem.setImageUrl(product.getImageUrl());
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);
        }

        return "redirect:/cart";
    }

    // ✅ Show cart page
    @GetMapping("/cart")
    public String showCart(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        double total = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }

    // ✅ Update quantity of cart item
    @PostMapping("/cart/update/{id}")
    public String updateQuantity(@PathVariable Long id,
                                 @RequestParam("quantity") int quantity,
                                 Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName());
        CartItem item = cartItemRepository.findByUserAndProduct_Id(user, id);

        if (item != null && quantity > 0) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return "redirect:/cart";
    }

    // ✅ Remove item from cart
    @PostMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName());
        CartItem item = cartItemRepository.findByUserAndProduct_Id(user, id);

        if (item != null) {
            cartItemRepository.delete(item);
        }

        return "redirect:/cart";
    }

    // ✅ Checkout page with all cart items (GET)
    @GetMapping("/checkout")
    public String checkoutPage(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login?error=user-not-found";
        }
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        double total = cartItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();

        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam(value = "selectedProductIds", required = false) List<Long> selectedIds,
                                  Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        if (selectedIds == null || selectedIds.isEmpty()) {
            return "redirect:/cart?error=nothing-selected";
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login?error=user-not-found";
        }

        List<CartItem> selectedItems = cartItemRepository.findByUser(user).stream()
                .filter(item -> selectedIds.contains(item.getProduct().getId()))
                .toList();

        double total = selectedItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();

        List<Product> products = selectedItems.stream()
                .map(CartItem::getProduct)
                .toList();

        List<Integer> quantities = selectedItems.stream()
                .map(CartItem::getQuantity)
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("products", products);
        model.addAttribute("quantities", quantities);
        model.addAttribute("total", total);

        return "checkout";
    }

}

package ecommerce_website.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ecommerce_website.model.Product;
import ecommerce_website.model.User;
import ecommerce_website.repository.ProductRepository;
import ecommerce_website.repository.UserRepository;
import ecommerce_website.service.CartService;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }
    @PostMapping("/upload")
    public String uploadProduct(@ModelAttribute Product product, Authentication auth) {
        User seller = userRepository.findByEmail(auth.getName());
        product.setSeller(seller);
        productRepository.save(product);
        return "redirect:/seller/dashboard";
    }
    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, Principal principal) {
        cartService.addProductToCart(id, principal.getName());
        return "redirect:/cart";
    }
}

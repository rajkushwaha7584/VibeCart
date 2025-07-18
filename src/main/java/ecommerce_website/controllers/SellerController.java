package ecommerce_website.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;
import ecommerce_website.model.Product;
import ecommerce_website.repository.ProductRepository;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/dashboard")
    public String sellerDashboard(Model model, Authentication auth) {
        String sellerEmail = auth.getName();
        List<Product> products = productRepository.findBySellerEmail(sellerEmail);
        model.addAttribute("products", products);
        return "seller/dashboard";
    }

    @PostMapping("/upload")
    public String uploadProduct(@ModelAttribute Product product, Authentication auth) {
        product.setSellerEmail(auth.getName());
        productRepository.save(product);
        return "redirect:/seller/dashboard";
    }
}

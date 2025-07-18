package ecommerce_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ecommerce_website.repository.ProductRepository;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }

}

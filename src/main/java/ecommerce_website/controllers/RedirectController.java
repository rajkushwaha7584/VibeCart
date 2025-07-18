package ecommerce_website.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/default")
    public String redirectBasedOnRole(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        switch (role) {
            case "ADMIN":
                return "redirect:/admin/dashboard";
            case "SELLER":
                return "redirect:/seller/dashboard";
            case "CUSTOMER":
                return "redirect:/customer/home";
            default:
                return "redirect:/";
        }
    }
}

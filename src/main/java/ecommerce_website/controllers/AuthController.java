package ecommerce_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ecommerce_website.model.User;
import ecommerce_website.service.UserService;

@Controller
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private AuthenticationManager authenticationManager;

    @GetMapping("/register")
    public String showRegister(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User()); // required for registration form
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        String result = userService.registerUser(user);
        if ("otp_sent".equals(result)) {
            model.addAttribute("email", user.getEmail());
            return "verify_otp";
        } else {
            model.addAttribute("error", result);
            model.addAttribute("user", new User()); // add this for retry
            return "register";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            RedirectAttributes redirectAttributes) {
        if (userService.verifyOtp(email, otp)) {
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("error", "❌ Invalid OTP! Try again.");
            return "redirect:/verify-otp";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@ModelAttribute("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify_otp";
    }


}

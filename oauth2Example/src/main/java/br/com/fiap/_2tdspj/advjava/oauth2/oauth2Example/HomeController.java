package br.com.fiap._2tdspj.advjava.oauth2.oauth2Example;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("name", principal.getName());
        model.addAttribute("anotherName", principal.getAttribute("name"));

        System.out.println(model.getAttribute("name"));
        System.out.println(model.getAttribute("anotherName"));
        System.out.println(principal.getAttributes());
        return "home";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

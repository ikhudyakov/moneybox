package ml.ilei.moneybox.controllers;

import ml.ilei.moneybox.domains.User;
import ml.ilei.moneybox.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class SettingController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/settings")
    public String settings(
            @AuthenticationPrincipal User user,
            Model model
    ) throws IOException {
        User currentUser = userRepository.findByUsername(user.getUsername());
        model.addAttribute("currentUser", currentUser);
        return "/settings";
    }

    @PostMapping("/settings")
    public String saveSettings(
            @AuthenticationPrincipal User user,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("idTelegram") String idTelegram,
            Model model
    ) throws IOException {
        User currentUser = userRepository.findByUsername(user.getUsername());
        if (!currentUser.getPassword().equals(password) && (!password.trim().equals(""))) {
            currentUser.setPassword(password.trim());
        }
        if (!currentUser.getEmail().equals(email)) {
            currentUser.setEmail(email.trim());
        }
        if (!currentUser.getIdTelegram().equals(idTelegram)) {
            currentUser.setIdTelegram(idTelegram.trim());
        }
        userRepository.save(currentUser);
        return "redirect:/settings";
    }
}

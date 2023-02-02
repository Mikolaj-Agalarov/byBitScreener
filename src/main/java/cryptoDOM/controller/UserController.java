package cryptoDOM.controller;

import cryptoDOM.dto.UserDto;
import cryptoDOM.model.User;
import cryptoDOM.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userRegistration(Model model) {
        return "registration";
    }

    @PostMapping
    public RedirectView userRegistration(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam String role,
                                         @RequestParam String email,
                                         Model model) {
        User newUser = new User(username, password, role, email);
        return new RedirectView("allUsers");
    }

    @GetMapping("/showAll")
    public String showAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "allUsers";
    }
}

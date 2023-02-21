package cryptoDOM.controller;

import cryptoDOM.entity.User;
import cryptoDOM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpServletRequest request) {
        try {
            User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());
            if(authenticatedUser != null) {
                request.getSession().setAttribute("user", authenticatedUser);
                return "redirect:/api/showData";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "login";
            }
        } catch(AuthenticationException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch(Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

}

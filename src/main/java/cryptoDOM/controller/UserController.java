package cryptoDOM.controller;

import cryptoDOM.dto.UserDto;
import cryptoDOM.model.User;
import cryptoDOM.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class UserController {
    private final UserService userService;

    @GetMapping
    protected String userRegistration(Model model) {
        model.addAttribute("dto", new UserDto());
        return "registration";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView userRegistration(@ModelAttribute("dto") final User dto) {
            userService.saveUser(dto);
            return new RedirectView("/users");
    }
}

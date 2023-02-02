package cryptoDOM.controller;

import cryptoDOM.dto.UserDtoRequest;
import cryptoDOM.dto.UserDtoResponse;
import cryptoDOM.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/registration")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userRegistration(Model model) {
        return "registration2";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView userRegistration(UserDtoRequest userDto) {
        userService.saveUser(userDto);
        return new RedirectView("allUsers");
    }

    @GetMapping("/showAll")
    public String showAllUsers(Model model) {
        List<UserDtoResponse> users = userService.findAll();
        model.addAttribute("users", users);
        return "allUsers";
    }
}

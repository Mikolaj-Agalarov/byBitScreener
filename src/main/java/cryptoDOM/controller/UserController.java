package cryptoDOM.controller;

import cryptoDOM.dto.UserDTO;
import cryptoDOM.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class UserController {

    @GetMapping("/registration")
    public String registrationResponse(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "registration";
    }


}

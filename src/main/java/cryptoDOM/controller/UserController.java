package cryptoDOM.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cryptoDOM.dto.UserDtoRequest;
import cryptoDOM.dto.UserDtoResponse;
import cryptoDOM.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/registration")
    public String userRegistration(Model model) {
        return "registration2";
    }

    @PostMapping(value = "/registration")
    public RedirectView userRegistration(UserDtoRequest userDto,Model model) {

        userService.saveUser(userDto);
        List<UserDtoResponse> users = userService.findAll();
        model.addAttribute("users", users);
        return new RedirectView("allUsers");
    }

    @GetMapping("/allUsers")
    public String showAllUsers(Model model, HttpServletRequest request) {
        var session = request.getSession();
        var attr = session.getAttribute("valid");
        if(attr!=null&&Boolean.parseBoolean(attr.toString()) ) {
            List<UserDtoResponse> users = userService.findAll();
            model.addAttribute("users", users);
        }
        else {
            model.addAttribute("users", new ArrayList<>());
        }
        return "allUsers";
    }
    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping(value = "/logout")
    public String loginPage(HttpServletRequest request) {
        var session = request.getSession();
        session.invalidate();
        return "logout";
    }
    @PostMapping(value = "login")
    public RedirectView login(HttpServletRequest request, UserDtoRequest userDto){
        var session = request.getSession();
        if(userService.isValidEmailAndPassword(userDto.getEmail(),userDto.getPassword()));
        session.setAttribute("valid",true);
        return new RedirectView("allUsers");
    }

    @GetMapping(value = "/test")
    public ResponseEntity<Object> test(){
        ObjectNode obj =(ObjectNode) JsonNodeFactory.instance.objectNode();
        obj.put("sasi","xui");
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
}

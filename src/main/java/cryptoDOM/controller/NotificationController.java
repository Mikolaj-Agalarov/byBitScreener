package cryptoDOM.controller;

import cryptoDOM.entity.Notification;
import cryptoDOM.entity.TickerName;
import cryptoDOM.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/showNotifications")
    public String showNotifications(Model model) {
        List<Notification> notifications = notificationService.findAllByOrderByCreatedAtDesc();
        model.addAttribute("notifications", notifications);

        return "notifications";
    }

}

package main.java.com.byBitScreener.controller;

import main.java.com.byBitScreener.dto.depthDto.DepthDto;
import main.java.com.byBitScreener.service.ByBitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/")
public class mainController {
    ByBitService byBitService = new ByBitService();

    @GetMapping
    public String getPrice (Model model) throws Exception {
        ArrayList<DepthDto> arrayWithDOMs = byBitService.getDepthOfMarket();
//        for (DepthDto glass : glasses) {
//            model
//        }

        model.addAttribute("glass", arrayWithDOMs);
        return "/mainPage";

    }

}

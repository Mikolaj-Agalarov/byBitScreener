package cryptoDOM.controller;

import cryptoDOM.dto.kuCoinDtos.DOMDto.GlassInstance;
import cryptoDOM.dto.kuCoinDtos.DOMDto.KuCoinDepthDto;
import cryptoDOM.service.kuCoinServices.KuCoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
//@RequestMapping("/kuCoinScreener")
@RequestMapping("/kuCoinScreener")
public class KuCoinController {
    KuCoinService kuCoinService = new KuCoinService();

//    @GetMapping("/asksAndBids")
    @GetMapping("/bidsAndAsks")
    public String getBidsAndAsks(Model model) throws Exception {
        List<GlassInstance> ListOfGlassInstances = kuCoinService.getDepthOfMarket(50, 30000);
        model.addAttribute("ListOfGlassInstances", ListOfGlassInstances);
        return "/kuCoinBidsAndAsks.html";
    }
    @GetMapping("/bids")
    public String getBids(Model model) throws Exception {
        List<GlassInstance> ListOfGlassInstances = kuCoinService.getDepthOfMarket(10, 30000);
        model.addAttribute("ListOfGlassInstances", ListOfGlassInstances);
        return "/kuCoinBids.html";
    }
}

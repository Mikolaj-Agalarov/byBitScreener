package cryptoDOM.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cryptoDOM.entity.Ask;
import cryptoDOM.entity.Bid;
import cryptoDOM.entity.DOM;
import cryptoDOM.entity.TickerName;

import cryptoDOM.service.AskService;
import cryptoDOM.service.BidService;
import cryptoDOM.service.DOMService;
import cryptoDOM.service.TickerNameService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api")
public class TickerDataRestController {
    private final TickerNameService tickerNameService;
    private final AskService askService;
    private final BidService bidService;
    private final DOMService domService;

    public TickerDataRestController(TickerNameService tickerNameService, AskService askService, BidService bidService, DOMService domServiced) {
        this.tickerNameService = tickerNameService;
        this.askService = askService;
        this.bidService = bidService;
        this.domService = domServiced;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getdata")
    public void getData(Model model) throws Exception {
        //Getting JSON and converting it into a String from response
        RestTemplate restTemplate = new RestTemplate();

        Gson gson = new Gson();
        String url = "https://openapi-v2.kucoin.com/api/v1/market/allTickers";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String json = response.getBody();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        JsonArray tickers = data.get("ticker").getAsJsonArray();

        System.out.println("TickerNames table uptade started");

        tickerNameService.updateDB(tickers);

        System.out.println("TickerNames table updated");

        System.out.println("DOMS table cleared");

        domService.updateDomsFromAPI();

        System.out.println("DOMS table updated");

    }

    @GetMapping("/showData")
    public String testData (Model model) {
        List<Ask> asks = askService.findInDescOrder();

        List<Bid> bids =bidService.findInDescOrder();

        List<DOM> doms = new ArrayList<>();

        for (Bid b:bids) {
            if (doms.contains(domService.getDomById(b.getDom().getId()).get())){
                continue;
            }
            if (domService.getDomById(b.getDom().getId()).isPresent()) {
                DOM dom = domService.getDomById(b.getDom().getId()).get();
                doms.add(dom);

            }
        }

        for (Ask a:asks) {
            if (doms.contains(domService.getDomById(a.getDom().getId()).get())){
                continue;
            }
            if (domService.getDomById(a.getDom().getId()).isPresent() && !doms.contains(domService.getDomById(a.getDom().getId()).get())) {
                DOM dom = domService.getDomById(a.getDom().getId()).get();
                doms.add(dom);
            }
        }

        model.addAttribute("doms", doms);

        return "showData";
    }
}

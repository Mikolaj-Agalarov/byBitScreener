package cryptoDOM.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cryptoDOM.dto.kuCoinDtos.symbolsDto.KuCoinTickerDto;
import cryptoDOM.entity.Ask;
import cryptoDOM.entity.Bid;
import cryptoDOM.entity.DOM;
import cryptoDOM.entity.TickerName;
import cryptoDOM.mapper.TickerMapper;
import cryptoDOM.service.AskService;
import cryptoDOM.service.BidService;
import cryptoDOM.service.DOMService;
import cryptoDOM.service.TickerNameService;
import io.micrometer.core.instrument.util.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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
    public String showData (Model model) {

        List<DOM> doms = domService.getDOMsWithAsksOrBids();
        List<TickerName> tickerNames = tickerNameService.getAllTickerNames();


        model.addAttribute("doms", doms);
        model.addAttribute("tickerNames", tickerNames);

        return "test";
    }
}

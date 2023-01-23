package cryptoDOM;


import cryptoDOM.dto.kuCoinDtos.DOMDto.GlassInstance;
import cryptoDOM.service.byBitServices.ByBitService;
import cryptoDOM.service.kuCoinServices.KuCoinService;

import java.util.List;

public class main {
    public static void main(String[] args) throws Exception {
        ByBitService byBitService = new ByBitService();
        KuCoinService kuCoinService = new KuCoinService();
//        byBitService.getAllSpotTickersFromApiAndWriteThemToTxt();
//        byBitService.getTickersFromTxtToArrayList();
//        byBitService.getDepthOfMarket();
//        kuCoinService.getAllSpotTickersFromApiAndWriteThemToTxt();
        List<GlassInstance> result = kuCoinService.getDepthOfMarket(50, 100000);
        System.out.println(result);
    }

}

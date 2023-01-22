package main.resources;

import main.java.com.cryptoDOM.service.byBitServices.ByBitService;

public class main {
    public static void main(String[] args) throws Exception {
        ByBitService service = new ByBitService();
//        service.getAllSpotTickersFromApiAndWriteThemToTxt();
//        service.getTickersFromTxtToArrayList();
        service.getDepthOfMarket();
    }

}

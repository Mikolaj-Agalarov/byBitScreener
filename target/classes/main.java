package main.resources;

import main.java.com.byBitScreener.service.ByBitService;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws Exception {
        ByBitService service = new ByBitService();
//        service.getAllSpotTickersFromApiAndWriteThemToTxt();
//        service.getTickersFromTxtToArrayList();
        service.getDepthOfMarket();
    }

}

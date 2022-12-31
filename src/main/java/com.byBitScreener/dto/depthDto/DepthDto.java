package main.java.com.byBitScreener.dto.depthDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DepthDto {
    private String tickerName;
    private Map<Float, Float> asks;
    private Map<Float, Float> bids;
}

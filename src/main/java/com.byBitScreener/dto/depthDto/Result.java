package main.java.com.byBitScreener.dto.depthDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result {
    private Float[][] bids;
    private Float[][] asks;
    private Long time;
}

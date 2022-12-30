package main.java.com.byBitScreener.dto.depth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result {
    private Bid[] bid;
    private Ask[] ask;
    private Time[] time;
}

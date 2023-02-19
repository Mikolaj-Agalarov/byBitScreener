package cryptoDOM.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DOMS")
public class DOM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "dom")
    private TickerName tickerName;

    @Column(name = "highest_bid_price")
    private Float highest_bid_price;

    @Column(name = "lowest_ask_price")
    private Float lowest_ask_price;

    @OneToMany(mappedBy = "dom", cascade = CascadeType.ALL)
    private List<Ask> asks;

    @OneToMany(mappedBy = "dom", cascade = CascadeType.ALL)
    private List<Bid> bids;

}

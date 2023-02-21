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
    @Column(name = "ticker_id",insertable = false,updatable = false)
    private Long tickerId;
    @ManyToOne()
    @JoinColumn(name = "ticker_id", referencedColumnName = "id")
    private TickerName tickerName;

    @Column(name = "highest_bid_price")
    private BigDecimal highest_bid_price;

    @Column(name = "lowest_ask_price")
    private BigDecimal lowest_ask_price;

    @OneToMany(mappedBy = "dom", cascade = CascadeType.ALL)
    private List<Ask> asks;

    @OneToMany(mappedBy = "dom", cascade = CascadeType.ALL)
    private List<Bid> bids;

}

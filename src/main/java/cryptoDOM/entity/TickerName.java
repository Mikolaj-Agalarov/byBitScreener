package cryptoDOM.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "TICKER_NAMES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TickerName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ticker_name", unique = true)
    private String tickerName;

    @Column(name = "min_order_value", columnDefinition = "integer default 30000")
    private BigDecimal minOrderValue;

    @Column(name = "is_on", columnDefinition = "boolean default true")
    private Boolean isOn;

    @Column(name = "vol")
    private BigDecimal vol;

    @Column(name = "vol_value")
    private BigDecimal volValue;

    @OneToOne(mappedBy = "tickerName", cascade = CascadeType.ALL)
    private DOM dom;
}

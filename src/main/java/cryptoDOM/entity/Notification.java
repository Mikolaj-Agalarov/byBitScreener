package cryptoDOM.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "is_ask")
    private Boolean isAsk;

    @Column(name = "is_bid")
    private Boolean isBid;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private LocalDateTime createdAt;

    @ManyToOne()
    @JoinColumn(name = "ticker_name", referencedColumnName = "ticker_name")
    private TickerName tickerName;

    @ManyToOne
    @JoinColumn(name = "dom_id")
    private DOM dom_id;


}

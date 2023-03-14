package cryptoDOM.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "asks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Ask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "range")
    private BigDecimal range;

    @Column(name = "percentage_of_daily_volume")
    private BigDecimal percentageOfDailyVolume;

    @Column(name = "volume_in_usd")
    private BigDecimal volumeInUsd;

    @ManyToOne
    @JoinColumn(name = "dom_id")
    @ToString.Exclude
    private DOM dom;

}

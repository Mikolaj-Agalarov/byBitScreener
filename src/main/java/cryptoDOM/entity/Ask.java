package cryptoDOM.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "asks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @ManyToOne
    @JoinColumn(name = "dom_id")
    private DOM dom;

}

package cryptoDOM.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "bids")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "range")
    private BigDecimal range;

    @ManyToOne
    @JoinColumn(name = "dom_id")
    private DOM dom;
}

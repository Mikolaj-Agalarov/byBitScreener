package cryptoDOM.repository;

import cryptoDOM.entity.Ask;
import cryptoDOM.entity.Bid;
import cryptoDOM.entity.DOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository <Bid, Long> {
    @Query("SELECT b FROM Bid b WHERE b.dom = :dom")
    List<Bid> findByDom(@Param("dom") DOM dom);
}

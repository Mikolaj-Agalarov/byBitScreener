package cryptoDOM.repository;

import cryptoDOM.entity.DOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DOMRepository extends JpaRepository<DOM, Long> {
    @Query("SELECT d FROM DOM d WHERE size(d.asks) > 0 OR size(d.bids) > 0")
    List<DOM> findByAsksIsNotNullOrBidsIsNotNull();
    @Query("select d from DOM d where d.tickerName.tickerName = ?1")
    DOM findByTickerName(String tickerName);

    Optional<DOM> findById(Long id);
}

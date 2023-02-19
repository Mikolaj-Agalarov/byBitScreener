package cryptoDOM.repository;

import cryptoDOM.entity.DOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DOMRepository extends JpaRepository<DOM, Long> {
    @Query("SELECT d FROM DOM d WHERE size(d.asks) > 0 OR size(d.bids) > 0")
    List<DOM> findByAsksIsNotNullOrBidsIsNotNull();
}

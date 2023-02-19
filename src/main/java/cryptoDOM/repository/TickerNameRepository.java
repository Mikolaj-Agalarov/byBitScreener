package cryptoDOM.repository;

import cryptoDOM.entity.TickerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TickerNameRepository extends JpaRepository<TickerName, Long> {
    @Query("select t from TickerName t where t.tickerName=?1")
    TickerName findBy(String tickerName);

    @Query("SELECT t FROM TickerName t WHERE t.isOn = true")
    List<TickerName> findByIsOnTrue();
}

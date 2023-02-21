package cryptoDOM.repository;

import cryptoDOM.entity.Notification;
import cryptoDOM.entity.TickerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.tickerName = :tickerName")
    List<Notification> findByTickerName(@Param("tickerName") TickerName tickerName);

    List<Notification> findAllByOrderByCreatedAtDesc();
}

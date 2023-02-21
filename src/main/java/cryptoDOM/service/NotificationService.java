package cryptoDOM.service;

import cryptoDOM.entity.DOM;
import cryptoDOM.entity.Notification;
import cryptoDOM.entity.TickerName;
import cryptoDOM.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private  NotificationRepository notificationRepository;
    @Transactional
    public void createNotificationIfNeeded(TickerName tickerName, BigDecimal price, BigDecimal amount, DOM dom) {
        List<Notification> existingNotifications = notificationRepository.findByTickerName(tickerName);

        Optional<Notification> existingNotification = existingNotifications.stream()
                .filter(notification -> notification.getPrice().equals(price))
                .filter(notification -> notification.getCreatedAt().isAfter(LocalDateTime.now().minusHours(2)))
                .findFirst();


        if (existingNotification.isPresent()) {
            // A notification already exists for this price and was created within the last hour
            System.out.println("Notification for price " + price + " already exists and was created within the last 2 hours.");
        } else {
            Notification notification = new Notification();
            notification.setTickerName(tickerName);
            notification.setPrice(price);
            notification.setValue(amount);
            notification.setDom_id(dom);
            notification.setCreatedAt(LocalDateTime.now());

            if (price.compareTo(dom.getHighest_bid_price()) > 0 && price.compareTo(dom.getLowest_ask_price()) >= 0) {
                notification.setIsAsk(true);
                notification.setIsBid(false);
            } else {
                notification.setIsAsk(false);
                notification.setIsBid(true);
            }
            System.out.println("code executed here ");

            try {
                notificationRepository.save(notification);
                System.out.println("New notification for " + tickerName.getTickerName() + " at level " + price + " created.");
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("New notification for " + tickerName.getTickerName() + " at level " + price + " created.");
        }

    }

    public List<Notification> findAllByOrderByCreatedAtDesc() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }


}

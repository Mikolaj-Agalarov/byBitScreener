package cryptoDOM.repository;

import cryptoDOM.entity.Ask;
import cryptoDOM.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AskRepository extends JpaRepository<Ask, Long> {
    @Modifying
    @Query("DELETE FROM Ask a WHERE a = :ask")
    void deleteAsk(@Param("ask") Ask ask);

    @Query("SELECT a FROM Ask a WHERE a.dom.id = :domId")
    List<Ask> findByDomId(@Param("domId") Long domId);

    List<Ask> findAllByOrderByVolumeInUsdDesc();

}

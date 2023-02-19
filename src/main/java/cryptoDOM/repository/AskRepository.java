package cryptoDOM.repository;

import cryptoDOM.entity.Ask;
import cryptoDOM.entity.DOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AskRepository extends JpaRepository<Ask, Long> {

}

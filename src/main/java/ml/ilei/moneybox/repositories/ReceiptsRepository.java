package ml.ilei.moneybox.repositories;

import ml.ilei.moneybox.domains.Receipts;
import ml.ilei.moneybox.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface ReceiptsRepository extends JpaRepository<Receipts, Long> {
    Page<Receipts> findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(User user, Date start, Date end, Pageable pageable);
    List<Receipts> findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(User user, Date start, Date end);
    Page<Receipts> findAllByUser(User user, Pageable pageable);
    List<Receipts> findAllByUser(User user);
}
package ml.ilei.moneybox.repositories;

import ml.ilei.moneybox.domains.Costs;
import ml.ilei.moneybox.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface CostsRepository extends JpaRepository<Costs, Long> {
    Page<Costs> findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(User user, Date start, Date end, Pageable pageable);
    List<Costs> findAllByUserAndDateGreaterThanEqualAndDateLessThanEqual(User user, Date start, Date end);
    Page<Costs> findAllByUser(User user, Pageable pageable);
    List<Costs> findAllByUser(User user);
}
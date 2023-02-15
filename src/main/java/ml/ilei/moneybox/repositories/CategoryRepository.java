package ml.ilei.moneybox.repositories;

import ml.ilei.moneybox.domains.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByType(String type);
}

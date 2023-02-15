package ml.ilei.moneybox.repositories;

import ml.ilei.moneybox.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

package io.automation.telegram.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import io.automation.telegram.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
}

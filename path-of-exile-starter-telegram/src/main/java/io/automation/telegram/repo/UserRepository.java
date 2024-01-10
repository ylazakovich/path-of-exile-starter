package io.automation.telegram.repo;

import io.automation.telegram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findById(long id);
}

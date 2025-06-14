package io.starter.repo;

import io.starter.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  UserEntity findById(long id);

  UserEntity findByUserId(long id);
}

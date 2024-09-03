package io.starter.telegram.repo;

import io.starter.telegram.entity.RegressionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegressionRepository extends JpaRepository<RegressionEntity, Long> {

  RegressionEntity findById(long id);
}

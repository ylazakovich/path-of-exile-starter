package io.starter.repo;

import io.starter.entity.RegressionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegressionRepository extends JpaRepository<RegressionEntity, Long> {

  RegressionEntity findById(long id);
}

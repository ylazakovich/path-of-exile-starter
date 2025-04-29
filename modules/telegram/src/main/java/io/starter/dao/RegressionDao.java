package io.starter.dao;

import io.starter.entity.RegressionEntity;
import io.starter.repo.RegressionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegressionDao {

  private final RegressionRepository regressionRepository;

  @Autowired
  public RegressionDao(RegressionRepository regressionRepository) {
    this.regressionRepository = regressionRepository;
  }

  public RegressionEntity read(long id) {
    return regressionRepository.findById(id);
  }

  public RegressionEntity update(RegressionEntity entity) {
    return regressionRepository.save(entity);
  }
}

package io.starter.controller;

import io.starter.dao.RegressionDao;
import io.starter.entity.RegressionEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RegressionController {

  private final RegressionDao regressionDao;

  public RegressionController(RegressionDao regressionDao) {
    this.regressionDao = regressionDao;
  }

  @GetMapping("/regression/code")
  public ResponseEntity read() {
    log.info("Read verification code for running regression");
    RegressionEntity entity = regressionDao.read(1);
    return ResponseEntity.ok().body(entity);
  }

  @PutMapping("/regression/code/expire")
  public ResponseEntity update() {
    log.info("Expire verification code");
    RegressionEntity entity = regressionDao.read(1);
    entity.setCode("00000");
    entity.setIsNotUsed(false);
    return ResponseEntity.ok().body(entity);
  }
}

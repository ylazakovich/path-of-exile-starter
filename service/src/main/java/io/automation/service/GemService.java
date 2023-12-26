package io.automation.service;

import java.util.List;

import io.automation.entity.GemEntity;
import io.automation.ex.GemNotFoundException;
import io.automation.repo.GemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GemService {

  private final GemRepo gemRepo;

  @Autowired
  public GemService(GemRepo gemRepo) {
    this.gemRepo = gemRepo;
  }

  public GemEntity addGem(GemEntity gemEntity) {
    return gemRepo.save(gemEntity);
  }

  public List<GemEntity> findAllGems() {
    return gemRepo.findAll();
  }

  public GemEntity updateGem(GemEntity gemEntity) {
    return gemRepo.save(gemEntity);
  }

  public GemEntity findGemById(Long id) {
    return gemRepo.findGemById(id).orElseThrow(() -> new GemNotFoundException("Gem by id " + id + "was not found"));
  }

  public void deleteGem(Long id) {
    gemRepo.deleteGemById(id);
  }
}

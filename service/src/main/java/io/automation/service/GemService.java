package io.automation.service;

import java.util.List;

import io.automation.entity.Gem;
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

  public Gem addGem(Gem gem) {
    return gemRepo.save(gem);
  }

  public List<Gem> findAllGems() {
    return gemRepo.findAll();
  }

  public Gem updateGem(Gem gem) {
    return gemRepo.save(gem);
  }

  public Gem findGemById(Long id) {
    return gemRepo.findGemById(id).orElseThrow(() -> new GemNotFoundException("Gem by id " + id + "was not found"));
  }

  public void deleteGem(Long id) {
    gemRepo.deleteGemById(id);
  }
}

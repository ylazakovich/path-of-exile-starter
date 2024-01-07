package io.automation.service;

import java.util.List;

import io.automation.dto.GemDTO;
import io.automation.entity.GemEntity;
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

  public List<GemEntity> findAllGems() {
    return gemRepo.findAll();
  }

  public void saveAll(List<GemEntity> gemEntities) {
    gemRepo.saveAll(gemEntities);
  }

  public void saveAll(GemDTO data) {
    List<GemEntity> entityList = GemDTO.convertToEntity(data.getLines());
    gemRepo.saveAll(entityList);
  }

  public void deleteAll() {
    gemRepo.truncateTable();
  }
}

package io.starter.service;

import java.util.List;
import java.util.Objects;

import io.starter.entity.SkillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final SkillsService skillsService;
  private final PoeNinjaService poeNinjaService;

  @Autowired
  public DatabaseService(SkillsService skillsService,
                         PoeNinjaService poeNinjaService) {
    this.skillsService = skillsService;
    this.poeNinjaService = poeNinjaService;
  }

  public void loadSkills() {
    if (skillsService.findAll().isEmpty()) {
      poeNinjaService.getSkills().subscribe(data -> skillsService.saveAll(data.getBody()));
    }
  }

  public void refreshSkills() {
    List<SkillEntity> skillsBefore = skillsService.findAll();
    poeNinjaService.getSkills().subscribe(data -> {
      skillsBefore.forEach(priceBefore ->
          Objects.requireNonNull(data.getBody()).getLines().stream()
              .filter(priceAfter -> priceAfter.getName().equals(priceBefore.getName()) &&
                  priceAfter.getVariant().equals(priceBefore.getVariant()))
              .findFirst()
              .ifPresent(matchedEntity -> priceBefore.setChaosValue(matchedEntity.getChaosValue()))
      );
      skillsService.saveAll(skillsBefore);
    });
  }
}

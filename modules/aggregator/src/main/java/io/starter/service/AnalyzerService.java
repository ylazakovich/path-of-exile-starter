package io.starter.service;

import java.util.List;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.dto.SkillDto;
import io.starter.repo.SkillsRepository;

import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {

  private final SkillsRepository skillsRepository;

  public AnalyzerService(SkillsRepository skillsRepository) {
    this.skillsRepository = skillsRepository;
  }

  public List<AnalyzedSkillDto> analyze() {
    List<SkillDto> data = SkillDto.convertToList(skillsRepository.findAll());
    List<SkillDto> maxQualitySkills = data.stream()
        .filter(skillDto -> skillDto.getVariant().equals("1/20") && !skillDto.isCorrupted())
        .toList();
    List<SkillDto> maxLevelSkills = data.stream()
        .filter(skillDto -> skillDto.getVariant().equals("20") && !skillDto.isCorrupted())
        .toList();
    return subtract(maxQualitySkills, maxLevelSkills);
  }

  private static List<AnalyzedSkillDto> subtract(List<SkillDto> quality, List<SkillDto> level) {
    return quality.stream()
        .filter(q -> level.stream().anyMatch(l -> l.getName().equals(q.getName())))
        .map(q -> {
          SkillDto matchingLevelGem = level.stream()
              .filter(l -> l.getName().equals(q.getName()))
              .findFirst()
              .orElse(null);
          if (matchingLevelGem != null) {
            String name = q.getName();
            double maxQualityPrice = q.getChaosEquivalentPrice();
            double maxLevelPrice = matchingLevelGem.getChaosEquivalentPrice();
            // TODO: craftCost should read from currency table
            return new AnalyzedSkillDto(name, 1.0, maxQualityPrice - maxLevelPrice);
          }
          return null;
        })
        .toList();
  }
}

package io.starter.service;

import java.util.List;

import io.starter.dto.SkillDTO;
import io.starter.dto.AnalyzedSkillDTO;
import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {

  private final SkillsService skillsService;

  public AnalyzerService(SkillsService skillsService) {
    this.skillsService = skillsService;
  }

  public List<AnalyzedSkillDTO> analyze() {
    List<SkillDTO> data = SkillDTO.convertToList(skillsService.findAll());
    List<SkillDTO> maxQualitySkills = data.stream()
        .filter(skillDTO -> skillDTO.getVariant().equals("1/20") && !skillDTO.isCorrupted())
        .toList();
    List<SkillDTO> maxLevelSkills = data.stream()
        .filter(skillDTO -> skillDTO.getVariant().equals("20") && !skillDTO.isCorrupted())
        .toList();
    return subtract(maxQualitySkills, maxLevelSkills);
  }

  private static List<AnalyzedSkillDTO> subtract(List<SkillDTO> quality, List<SkillDTO> level) {
    return quality.stream()
        .filter(q -> level.stream().anyMatch(l -> l.getName().equals(q.getName())))
        .map(q -> {
          SkillDTO matchingLevelGem = level.stream()
              .filter(l -> l.getName().equals(q.getName()))
              .findFirst()
              .orElse(null);
          if (matchingLevelGem != null) {
            String name = q.getName();
            double maxQualityPrice = q.getChaosValue();
            double maxLevelPrice = matchingLevelGem.getChaosValue();
            // TODO: craftCost should read from currency table
            return new AnalyzedSkillDTO(name, 1.0, maxQualityPrice - maxLevelPrice);
          }
          return null;
        })
        .toList();
  }
}

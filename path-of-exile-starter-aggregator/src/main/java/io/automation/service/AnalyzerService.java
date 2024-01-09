package io.automation.service;

import java.util.List;

import io.automation.dto.SkillDTO;
import io.automation.dto.AnalyzedSkillDTO;
import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {

  private final SkillGemService skillGemService;

  public AnalyzerService(SkillGemService skillGemService) {
    this.skillGemService = skillGemService;
  }

  public List<AnalyzedSkillDTO> analyze() {
    List<SkillDTO> data = SkillDTO.convertToList(skillGemService.findAllGems());
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

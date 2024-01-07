package io.automation.service;

import java.util.List;

import io.automation.dto.SkillGemDTO;
import io.automation.dto.TradeSkillGemDTO;
import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {

  private final SkillGemService skillGemService;

  public AnalyzerService(SkillGemService skillGemService) {
    this.skillGemService = skillGemService;
  }

  public List<TradeSkillGemDTO> analyze() {
    List<SkillGemDTO> data = SkillGemDTO.convertToList(skillGemService.findAllGems());
    List<SkillGemDTO> maxQualitySkills = data.stream()
        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("1/20") && !skillGemDTO.isCorrupted())
        .toList();
    List<SkillGemDTO> maxLevelSkills = data.stream()
        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("20") && !skillGemDTO.isCorrupted())
        .toList();
    return subtract(maxQualitySkills, maxLevelSkills);
  }

  private static List<TradeSkillGemDTO> subtract(List<SkillGemDTO> quality, List<SkillGemDTO> level) {
    return quality.stream()
        .filter(q -> level.stream().anyMatch(l -> l.getName().equals(q.getName())))
        .map(q -> {
          SkillGemDTO matchingLevelGem = level.stream()
              .filter(l -> l.getName().equals(q.getName()))
              .findFirst()
              .orElse(null);
          if (matchingLevelGem != null) {
            String name = q.getName();
            double maxQualityPrice = q.getChaosValue();
            double maxLevelPrice = matchingLevelGem.getChaosValue();
            return new TradeSkillGemDTO(name, 1.0, maxQualityPrice - maxLevelPrice);
          }
          return null;
        })
        .toList();
  }
}

package io.automation.controller;

import java.util.List;

import io.automation.dto.TradeSkillGemDTO;
import io.automation.dto.SkillGemDTO;
import io.automation.entity.SkillGemEntity;
import io.automation.service.SkillGemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gems")
public class SkillGemController {

  private final SkillGemService skillGemService;

  public SkillGemController(SkillGemService skillGemService) {
    this.skillGemService = skillGemService;
  }

  @GetMapping("/all")
  public List<SkillGemDTO> findAll() {
    List<SkillGemEntity> entities = skillGemService.findAllGems();
    return SkillGemDTO.convertToList(entities);
  }

  @GetMapping("/trade/{value}")
  public List<TradeSkillGemDTO> findAllForTrade(@PathVariable("value") long value) {
    return analyze().stream().filter(skill -> skill.getProfit() >= value).toList();
  }

  // TODO: move to Service
  public List<TradeSkillGemDTO> analyze() {
    List<SkillGemDTO> data = findAll();
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

package io.automation.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import io.automation.dto.SellSkillGemDTO;
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

  @GetMapping("/findAll")
  public List<SkillGemDTO> findAll() {
    List<SkillGemEntity> entities = skillGemService.findAllGems();
    return SkillGemDTO.convertToList(entities);
  }

  @GetMapping("/findAll/{value}")
  public List<SkillGemDTO> findAllWithProfitMoreThan(@PathVariable("value") long value) {
    List<SkillGemDTO> data = findAll();
    List<SkillGemDTO> maxLevelSkills =
        data.stream().filter(skillGemDTO -> skillGemDTO.getVariant().equals("20") && !skillGemDTO.isCorrupted())
            .toList();
    List<SkillGemDTO> maxQualitySkills =
        data.stream().filter(skillGemDTO -> skillGemDTO.getVariant().equals("1/20") && !skillGemDTO.isCorrupted())
            .toList();
    return maxLevelSkills.stream()
        .filter(skillGemDTO -> maxQualitySkills.stream()
            .anyMatch(quality -> skillGemDTO.getName().equals(quality.getName()) &&
                (quality.getChaosValue() - skillGemDTO.getChaosValue() >= value)))
        .toList();
  }

  // TODO: Not finished
  @GetMapping("/sell")
  public List<SellSkillGemDTO> findAllSkillForSell() {
    List<SkillGemDTO> data = findAll();
    List<SkillGemDTO> maxQualitySkills = data.stream()
        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("1/20") && !skillGemDTO.isCorrupted())
        .toList();
    List<SkillGemDTO> maxLevelSkills = data.stream()
        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("20") && !skillGemDTO.isCorrupted())
        .filter(skillGemDTO -> maxQualitySkills.stream()
            .anyMatch(quality -> quality.getName().equals(skillGemDTO.getName())))
        .toList();
    return substract(maxQualitySkills, maxLevelSkills);
  }

  private static List<SellSkillGemDTO> substract(List<SkillGemDTO> quality, List<SkillGemDTO> level) {
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
            return new SellSkillGemDTO(name, 1.0, maxQualityPrice - maxLevelPrice);
          }
          return null;
        })
        .toList();
  }
}

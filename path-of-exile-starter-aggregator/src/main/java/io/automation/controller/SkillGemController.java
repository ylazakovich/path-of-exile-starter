package io.automation.controller;

import java.util.List;

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
    List<SkillGemDTO> maxLevelSkillGemDTOS = data.stream()
        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("20") && !skillGemDTO.isCorrupted())
        .toList();
    List<SkillGemDTO> maxQualitySkillGemDTOS = data.stream()
        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("1/20") && !skillGemDTO.isCorrupted())
        .toList();
    return maxLevelSkillGemDTOS.stream()
        .filter(skillGemDTO -> maxQualitySkillGemDTOS.stream()
            .anyMatch(quality -> skillGemDTO.getName().equals(quality.getName())
                && (quality.getChaosValue() - skillGemDTO.getChaosValue() >= value)))
        .toList();
  }
}

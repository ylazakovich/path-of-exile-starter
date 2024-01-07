package io.automation.controller;

import io.automation.service.SkillGemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gems")
public class GemController {

  private final SkillGemService skillGemService;

  public GemController(SkillGemService skillGemService) {
    this.skillGemService = skillGemService;
  }

//  @GetMapping("/findAll")
//  public List<SkillGem> findAll() {
//    List<SkillGemEntity> entities = gemService.findAllGems();
//    return SkillGemLinesDTO.convertToList(entities);
//  }
//
//  @GetMapping("/findAll/{value}")
//  public List<SkillGemLinesDTO.SkillGemDTO> findAllWithProfitMoreThan(@PathVariable("value") long value) {
//    List<SkillGemLinesDTO.SkillGemDTO> data = SkillGemLinesDTO.convertToList(gemService.findAllGems());
//    List<SkillGemLinesDTO.SkillGemDTO> maxLevelSkillGemDTOS = data.stream()
//        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("20") && !skillGemDTO.isCorrupted())
//        .toList();
//    List<SkillGemLinesDTO.SkillGemDTO> maxQualitySkillGemDTOS = data.stream()
//        .filter(skillGemDTO -> skillGemDTO.getVariant().equals("1/20") && !skillGemDTO.isCorrupted())
//        .toList();
//    return maxLevelSkillGemDTOS.stream()
//        .filter(skillGemDTO -> maxQualitySkillGemDTOS.stream()
//            .anyMatch(quality -> skillGemDTO.getName().equals(quality.getName())
//                && (quality.getChaosValue() - skillGemDTO.getChaosValue() >= value)))
//        .toList();
//  }
}

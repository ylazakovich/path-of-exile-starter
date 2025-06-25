package io.starter.controller;

import java.util.List;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.service.SkillDeltaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skills/delta")
@RequiredArgsConstructor
@Log4j2
public class SkillDeltaController {

  private final SkillDeltaService skillDeltaService;

  // TODO: Expected profit can be manage over telegram setting
  @GetMapping
  public List<AnalyzedSkillDto> getSkillsByLeague(@RequestParam(value = "league") String league) {
    return getDataWithProfit(league);
  }

  private List<AnalyzedSkillDto> getDataWithProfit(String league) {
    List<AnalyzedSkillDto> analyzed = skillDeltaService.analyzeSkills(league)
        .stream()
        .filter(skill -> skill.getChaosEquivalentProfit() >= 5)
        .toList();
    log.info("Skill delta analysis completed for league '{}'. {} results passed profit filter.", league, analyzed.size());
    return analyzed;
  }
}

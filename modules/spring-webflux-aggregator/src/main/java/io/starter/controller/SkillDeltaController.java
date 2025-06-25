package io.starter.controller;

import java.util.List;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.service.SkillDeltaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skills/delta")
public class SkillDeltaController {

  private final SkillDeltaService skillDeltaService;

  public SkillDeltaController(SkillDeltaService skillDeltaService) {
    this.skillDeltaService = skillDeltaService;
  }

  // TODO: Expected profit can be manage over telegram setting
  @GetMapping
  public List<AnalyzedSkillDto> getSkillsByLeague(@RequestParam(value = "league") String league) {
    return getSkillsByLeagueWithExpectedProfit(10, league);
  }

  private List<AnalyzedSkillDto> getSkillsByLeagueWithExpectedProfit(long value, String league) {
    return skillDeltaService.analyzeSkills(league)
        .stream()
        .filter(skill -> skill.getChaosEquivalentProfit() >= value)
        .toList();
  }
}

package io.starter.controller;

import java.util.List;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.service.AnalyzerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyzer")
public class AnalyzerController {

  private final AnalyzerService analyzerService;

  public AnalyzerController(AnalyzerService analyzerService) {
    this.analyzerService = analyzerService;
  }

  // TODO: Expected profit can be manage over telegram setting
  @GetMapping("/analyze/skills")
  public List<AnalyzedSkillDto> getSkillsByLeague(@RequestParam(value = "league") String league) {
    return getSkillsByLeagueWithExpectedProfit(10, league);
  }

  private List<AnalyzedSkillDto> getSkillsByLeagueWithExpectedProfit(long value, String league) {
    return analyzerService.analyzeSkills(league)
        .stream()
        .filter(skill -> skill.getChaosEquivalentProfit() >= value)
        .toList();
  }
}

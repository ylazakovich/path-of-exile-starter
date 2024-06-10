package io.starter.controller;

import java.util.List;

import io.starter.dto.AnalyzedSkillDTO;
import io.starter.service.AnalyzerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyzer")
public class AnalyzerController {

  private final AnalyzerService analyzerService;

  public AnalyzerController(AnalyzerService analyzerService) {
    this.analyzerService = analyzerService;
  }

  @GetMapping("/analyze/skills")
  public List<AnalyzedSkillDTO> findAllForTrade() {
    return findAllForTrade(10);
  }

  @GetMapping("analyze/skills/{takeProfit}")
  public List<AnalyzedSkillDTO> findAllForTrade(@PathVariable("takeProfit") long value) {
    return analyzerService.analyze().stream().filter(skill -> skill.getProfit() >= value).toList();
  }
}

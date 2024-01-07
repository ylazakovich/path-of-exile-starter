package io.automation.controller;

import java.util.List;

import io.automation.dto.TradeSkillGemDTO;
import io.automation.service.AnalyzerService;
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

  @GetMapping("/analyze/gems")
  public List<TradeSkillGemDTO> findAllForTrade() {
    return findAllForTrade(20);
  }

  @GetMapping("analyze/gems/{takeProfit}")
  public List<TradeSkillGemDTO> findAllForTrade(@PathVariable("takeProfit") long value) {
    return analyzerService.analyze().stream().filter(skill -> skill.getProfit() >= value).toList();
  }
}

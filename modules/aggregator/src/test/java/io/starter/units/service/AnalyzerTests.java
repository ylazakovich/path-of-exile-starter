package io.starter.units.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.starter.dto.AnalyzedSkillDTO;
import io.starter.service.AnalyzerService;
import io.starter.utils.Generator;
import org.testng.annotations.Test;

public class AnalyzerTests {

  private final AnalyzerService analyzerService = mock();

  @Test
  void analyzeTest() {
    List<AnalyzedSkillDTO> expectedList = List.of(Generator.generateAnalyzedSkill());
    when(analyzerService.analyze()).thenReturn(expectedList);

    List<AnalyzedSkillDTO> actualList = analyzerService.analyze();
    AnalyzedSkillDTO actualSkill = actualList.stream().findAny().get();
    assertThat(actualList).isEqualTo(expectedList);
    assertThat(actualSkill.getProfit()).isGreaterThan(actualSkill.getCraftCost());
  }
}

package io.starter.units.service.analyzer;

import java.util.List;
import java.util.Objects;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.service.AnalyzerService;
import io.starter.utils.Generator;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnalyzerServiceTests {

  private final AnalyzerService analyzerService = mock();

  @Test
  void analyzeAllSkillsTest() {
    List<AnalyzedSkillDto> expectedList = List.of(Generator.generateAnalyzedSkill());
    when(analyzerService.analyze()).thenReturn(expectedList);

    List<AnalyzedSkillDto> actualList = analyzerService.analyze();
    AnalyzedSkillDto actualSkill = actualList.stream().findAny().orElse(null);
    assertThat(actualList).isEqualTo(expectedList);
    assertThat(Objects.requireNonNull(actualSkill).getChaosEquivalentProfit()).isGreaterThan(actualSkill.getChaosEquivalentPrice());
  }
}

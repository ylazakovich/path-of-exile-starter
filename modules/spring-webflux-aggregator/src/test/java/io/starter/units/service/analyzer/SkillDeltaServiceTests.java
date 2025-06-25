package io.starter.units.service.analyzer;

import java.util.List;
import java.util.Objects;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.service.SkillDeltaService;
import io.starter.utils.SkillGenerator;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SkillDeltaServiceTests {

  private static final String queryParam = "Standard";
  private final SkillDeltaService skillDeltaService = mock();

  @Test
  void testServiceAnalyzeSkillsMethod() {
    final List<AnalyzedSkillDto> expectedList = List.of(SkillGenerator.generateProcessedSkill());
    when(skillDeltaService.analyzeSkills(queryParam)).thenReturn(expectedList);

    List<AnalyzedSkillDto> actualList = skillDeltaService.analyzeSkills(queryParam);
    AnalyzedSkillDto actualSkill = Objects.requireNonNull(actualList.stream().findAny().orElse(null));
    double actualProfit = actualSkill.getChaosEquivalentProfit();
    double actualPrice = actualSkill.getChaosEquivalentPrice();

    assertThat(actualList).isEqualTo(expectedList);
    assertThat(actualProfit).isGreaterThan(actualPrice);
  }
}

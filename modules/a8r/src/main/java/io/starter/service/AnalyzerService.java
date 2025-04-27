package io.starter.service;

import java.util.List;
import java.util.Objects;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.dto.SkillDto;
import io.starter.entity.LeagueEntity;

import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {

  private final DataAccessService dataAccessService;

  public AnalyzerService(DataAccessService dataAccessService) {
    this.dataAccessService = dataAccessService;
  }

  public List<AnalyzedSkillDto> analyze(String league) {
    LeagueEntity leagueEntity = dataAccessService.findLeagueByName(league);
    List<SkillDto> data = SkillDto.convertToList(dataAccessService.findSkillsByLeague(leagueEntity));
    List<SkillDto> maxQualitySkills = data.stream()
        .filter(skillDto -> skillDto.getVariant().equals("1/20") && !skillDto.isCorrupted())
        .toList();
    List<SkillDto> maxLevelSkills = data.stream()
        .filter(skillDto -> skillDto.getVariant().equals("20") && !skillDto.isCorrupted())
        .toList();
    return subtract(maxQualitySkills, maxLevelSkills, leagueEntity);
  }

  private List<AnalyzedSkillDto> subtract(List<SkillDto> quality,
                                          List<SkillDto> level,
                                          LeagueEntity leagueEntity) {
    return quality.stream()
        .filter(qlt -> level.stream()
            .anyMatch(lvl -> lvl.getName().equals(qlt.getName())))
        .map(qlt -> {
          SkillDto matchingLevelGem = level.stream()
              .filter(lvl -> lvl.getName().equals(qlt.getName()))
              .findFirst().orElse(null);
          if (Objects.nonNull(matchingLevelGem)) {
            long leagueId = qlt.getLeagueId();
            String name = qlt.getName();
            double maxQualityPrice = qlt.getChaosEquivalent();
            double maxLevelPrice = matchingLevelGem.getChaosEquivalent();
            AnalyzedSkillDto result = new AnalyzedSkillDto(leagueId, name, 1.0, maxQualityPrice - maxLevelPrice);
            dataAccessService.findRateByNameAndLeague("Gemcutter's Prism", leagueEntity)
                .ifPresent(result::setChaosEquivalentPrice);
            return result;
          }
          return null;
        }).toList();
  }
}

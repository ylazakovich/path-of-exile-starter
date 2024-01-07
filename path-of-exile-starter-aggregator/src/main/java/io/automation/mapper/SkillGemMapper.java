package io.automation.mapper;

import java.util.function.Function;

import io.automation.dto.SkillGemDTO;
import io.automation.model.SkillGem;

public class SkillGemMapper implements Function<SkillGem, SkillGemDTO> {

  @Override
  public SkillGemDTO apply(SkillGem skillGem) {
    return new SkillGemDTO(
        skillGem.getName(),
        skillGem.getVariant(),
        skillGem.isCorrupted(),
        skillGem.getGemLevel(),
        skillGem.getGemQuality(),
        skillGem.getChaosValue());
  }
}

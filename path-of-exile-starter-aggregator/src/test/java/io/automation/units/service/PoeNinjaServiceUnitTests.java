package io.automation.units.service;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.automation.model.Lines;
import io.automation.model.SkillGem;
import io.automation.service.PoeNinjaService;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

public class PoeNinjaServiceUnitTests {

  private final PoeNinjaService poeNinjaService = mock();

  @Test
  void getAllSkillGemsTest() {
    // TODO: continue
    when(poeNinjaService.getDataWithGems()).thenReturn(Mono.just(ResponseEntity.ok(new Lines<SkillGem>().setLines(List.of(new SkillGem())))));
    poeNinjaService.getDataWithGems().subscribe(data -> System.out.println());
  }
}

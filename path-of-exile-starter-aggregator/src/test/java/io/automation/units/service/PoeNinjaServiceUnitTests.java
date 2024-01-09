package io.automation.units.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.automation.model.Lines;
import io.automation.model.SkillGem;
import io.automation.service.PoeNinjaService;
import io.automation.utils.Generator;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

public class PoeNinjaServiceUnitTests {

  private final PoeNinjaService poeNinjaService = mock();

  @Test
  void getAllSkillGemsTest() {
    Lines<SkillGem> expected = Generator.generateLineWithSkills();
    when(poeNinjaService.getDataWithGems()).thenReturn(Mono.just(ResponseEntity.ok(expected)));

    poeNinjaService.getDataWithGems().subscribe(actual -> {
          Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
          Assertions.assertThat(actual.getBody()).isEqualTo(expected);
        }
    );
  }
}

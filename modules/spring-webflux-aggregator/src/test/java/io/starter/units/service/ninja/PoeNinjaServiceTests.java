package io.starter.units.service.ninja;

import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.model.ninja.UniqueJewel;
import io.starter.service.PoeNinjaService;
import io.starter.utils.CurrencyGenerator;
import io.starter.utils.SkillGenerator;
import io.starter.utils.UniqueJewelGenerator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PoeNinjaServiceTests {

  private static final String LEAGUE = "Standard";

  private final PoeNinjaService poeNinjaService = mock();

  @Test
  void testServiceGetSkillsMethod() {
    final Lines<Skill> expectedBody = SkillGenerator.generateLines();
    when(poeNinjaService.getSkills(LEAGUE)).thenReturn(Mono.just(ResponseEntity.ok(expectedBody)));
    poeNinjaService.getSkills(LEAGUE).subscribe(
        actual -> {
          assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
          assertThat(actual.getBody()).isEqualTo(expectedBody);
        }
    );
  }

  @Test
  void testServiceGetUniqueJewelMethod() {
    final Lines<UniqueJewel> expectedBody = UniqueJewelGenerator.generateLines();
    when(poeNinjaService.getUniqueJewels(LEAGUE)).thenReturn(Mono.just(ResponseEntity.ok(expectedBody)));
    poeNinjaService.getUniqueJewels(LEAGUE).subscribe(
        actual -> {
          assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
          assertThat(actual.getBody()).isEqualTo(expectedBody);
        }
    );
  }

  @Test
  void testServiceGetRateMethod() {
    final Lines<Currency> expectedBody = CurrencyGenerator.generateLines();
    when(poeNinjaService.getRates(LEAGUE)).thenReturn(Mono.just(ResponseEntity.ok(expectedBody)));
    poeNinjaService.getRates(LEAGUE).subscribe(
        actual -> {
          assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
          assertThat(actual.getBody()).isEqualTo(expectedBody);
        }
    );
  }
}

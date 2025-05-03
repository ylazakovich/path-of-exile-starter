package io.starter.service;

import io.starter.config.NinjaConfig;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;

import org.mockserver.configuration.Configuration;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.socket.tls.NettySslContextFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import reactor.netty.transport.ProxyProvider;

@Service
public class PoeNinjaService {

  private final WebClient client;

  public PoeNinjaService() {
    MockServerLogger mockServerLogger = new MockServerLogger(getClass());
    Configuration configuration = Configuration.configuration();
    boolean forServer = false;
    NettySslContextFactory sslContext = new NettySslContextFactory(configuration, mockServerLogger, forServer);
    this.client = WebClient.builder()
        .clientConnector(
            new ReactorClientHttpConnector(
                HttpClient
                    .create()
                    .secure(SslProvider.builder().sslContext(sslContext.createClientSslContext(true, false)).build())
                    .proxy(proxy -> proxy
                        .type(ProxyProvider.Proxy.HTTP)
                        .host("localhost")
                        .port(1080)
                    )
            )
        )
        .baseUrl(NinjaConfig.BASE_URL)
        .exchangeStrategies(ExchangeStrategies
            .builder()
            .codecs(
                codecs -> codecs
                    .defaultCodecs()
                    .maxInMemorySize(8 * 100 * 1024 * 1024))
            .build())
        .build();
  }

  public Mono<ResponseEntity<Lines<Skill>>> getSkills(String league) {
    return client.get()
        .uri("%s?%s".formatted(NinjaConfig.ITEM_ROUTE, "league=%s&type=SkillGem".formatted(league)))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {
        });
  }

  public Mono<ResponseEntity<Lines<Currency>>> getRates(String league) {
    return client.get()
        .uri("%s?%s".formatted(NinjaConfig.CURRENCY_ROUTE, "league=%s&type=Currency".formatted(league)))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {
        });
  }
}


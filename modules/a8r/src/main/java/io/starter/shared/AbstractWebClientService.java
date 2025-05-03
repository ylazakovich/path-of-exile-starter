package io.starter.shared;

import org.mockserver.configuration.Configuration;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.socket.tls.NettySslContextFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

public abstract class AbstractWebClientService {

  protected final WebClient client;

  protected AbstractWebClientService(boolean useProxy, String baseUrl) {
    this.client = buildWebClient(useProxy, baseUrl);
  }

  private WebClient buildWebClient(boolean useProxy, String baseUrl) {
    MockServerLogger mockServerLogger = new MockServerLogger(getClass());
    Configuration configuration = Configuration.configuration();
    boolean forServer = false;
    NettySslContextFactory sslContext = new NettySslContextFactory(configuration, mockServerLogger, forServer);

    HttpClient httpClient = HttpClient.create()
        .secure(sslSpec -> sslSpec.sslContext(sslContext.createClientSslContext(true, false)));

    if (useProxy) {
      httpClient = httpClient.proxy(proxy -> proxy
          .type(ProxyProvider.Proxy.HTTP)
          .host("localhost")
          .port(1080));
    }

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .baseUrl(baseUrl)
        .exchangeStrategies(ExchangeStrategies
            .builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(8 * 100 * 1024 * 1024))
            .build())
        .build();
  }
}

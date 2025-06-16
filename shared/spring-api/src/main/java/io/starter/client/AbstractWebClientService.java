package io.starter.client;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
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

  protected AbstractWebClientService(boolean useProxy, String baseUrl, String realUrl) {
    this.client = buildWebClient(useProxy, baseUrl, realUrl);
  }

  private WebClient buildWebClient(boolean useProxy, String baseUrl, String realUrl) {
    WebClient.Builder builder = WebClient.builder()
        .baseUrl(baseUrl)
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(8 * 1024 * 1024))
            .build());

    if (useProxy) {
      MockServerLogger mockServerLogger = new MockServerLogger(getClass());
      Configuration configuration = Configuration.configuration();
      NettySslContextFactory sslContext = new NettySslContextFactory(configuration, mockServerLogger, false);

      HttpClient proxyClient = HttpClient.create()
          .secure(sslSpec -> sslSpec.sslContext(sslContext.createClientSslContext(true, false)))
          .proxy(proxy -> proxy
              .type(ProxyProvider.Proxy.HTTP)
              .host("localhost")
              .port(1080));

      builder.clientConnector(new ReactorClientHttpConnector(proxyClient));
    }

    if (isMockServerUrl(baseUrl) && StringUtils.isNotBlank(realUrl)) {
      builder.defaultHeader(HttpHeaders.HOST, URI.create(realUrl).getHost());
    }

    return builder.build();
  }

  private boolean isMockServerUrl(String url) {
    return url.contains("localhost:1080") || url.contains("127.0.0.1:1080");
  }
}

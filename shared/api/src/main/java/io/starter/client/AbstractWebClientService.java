package io.starter.client;

import java.net.URI;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Slf4j
public abstract class AbstractWebClientService {

  @Getter
  protected final WebClient client;
  protected final RequestExecutor executor;

  protected AbstractWebClientService(boolean useProxy, String baseUrl, String realUrl) {
    this.client = buildWebClient(useProxy, baseUrl, realUrl);
    this.executor = new RequestExecutor(client);
  }

  protected <T> Mono<ResponseEntity<T>> get(String path,
                                            Map<String, String> queryParams,
                                            Map<String, String> headers,
                                            ParameterizedTypeReference<T> responseType) {
    return executor.get(path, queryParams, headers, responseType);
  }

  private WebClient buildWebClient(boolean useProxy, String baseUrl, String realUrl) {
    WebClient.Builder builder = WebClient.builder()
        .baseUrl(baseUrl)
        .filter(logExchange())
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(8 * 1024 * 1024))
            .build());
    if (useProxy) {
      HttpClient proxyClient = HttpClient.create()
          .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP).host("localhost").port(1080));
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

  private static ExchangeFilterFunction logExchange() {
    return ExchangeFilterFunction.ofRequestProcessor(request -> Mono.deferContextual(ctx -> {
      StringBuilder sb = new StringBuilder();
      sb.append("\n==================== [HTTP Request] ====================\n");
      sb.append("> ").append(request.method()).append(" ").append(request.url()).append("\n");
      request.headers().forEach((key, values) -> values.forEach(val ->
          sb.append("> ").append(key).append(": ").append(val).append("\n")
      ));
      return Mono.just(request).contextWrite(c -> c.put("requestLog", sb));
    })).andThen(ExchangeFilterFunction.ofResponseProcessor(response -> Mono.deferContextual(ctx -> {
      StringBuilder sb = ctx.getOrDefault("requestLog", new StringBuilder());
      sb.append("==================== [HTTP Response] ===================\n");
      sb.append("< Status: ").append(response.statusCode().value()).append(" ").append("\n");
      response.headers().asHttpHeaders().forEach((key, values) -> values.forEach(val ->
          sb.append("< ").append(key).append(": ").append(val).append("\n")
      ));
      return captureBodySize(response).flatMap(bodySize -> {
        sb.append("--------------------------------------------------------\n");
        sb.append("Body size: ").append(bodySize).append(" bytes\n");
        sb.append("========================================================");
        log.debug(sb.toString());
        return Mono.just(response);
      });
    })));
  }

  private static Mono<Integer> captureBodySize(ClientResponse response) {
    return response.bodyToFlux(DataBuffer.class)
        .map(dataBuffer -> {
          int size = dataBuffer.readableByteCount();
          DataBufferUtils.release(dataBuffer);
          return size;
        })
        .reduce(Integer::sum);
  }
}

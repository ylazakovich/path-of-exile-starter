package io.starter.client;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import io.starter.util.BodyWithSize;
import io.starter.util.CapturedResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.mockserver.configuration.Configuration;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.socket.tls.NettySslContextFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
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
                                            ParameterizedTypeReference<T> responseType) {
    return get(path, queryParams, Collections.emptyMap(), responseType);
  }

  protected <T> Mono<ResponseEntity<T>> get(String path,
                                            Map<String, String> queryParams,
                                            Map<String, String> headers,
                                            ParameterizedTypeReference<T> responseType) {
    return executor.get(path, queryParams, headers, responseType);
  }

  private WebClient buildWebClient(boolean useProxy, String baseUrl, String realUrl) {
    HttpClient httpClient = HttpClient.create().followRedirect(true);
    if (useProxy) {
      MockServerLogger mockServerLogger = new MockServerLogger(getClass());
      Configuration configuration = Configuration.configuration();
      NettySslContextFactory sslContext = new NettySslContextFactory(configuration, mockServerLogger, false);
      httpClient = HttpClient.create()
          .secure(sslSpec -> sslSpec.sslContext(sslContext.createClientSslContext(true, false)))
          .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP).host("localhost").port(1080))
          .followRedirect(true);
    }

    WebClient.Builder builder = WebClient.builder()
        .baseUrl(baseUrl)
        .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
        .filter(logExchange())
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(codecs -> {
              codecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
              ObjectMapper objectMapper = new ObjectMapper();
              codecs.customCodecs().register(
                  new Jackson2JsonDecoder(
                      objectMapper,
                      MediaType.APPLICATION_OCTET_STREAM
                  )
              );
            })
            .build());
    if (isMockServerUrl(baseUrl) && org.apache.commons.lang3.StringUtils.isNotBlank(realUrl)) {
      builder.defaultHeader(HttpHeaders.HOST, URI.create(realUrl).getHost());
    }
    builder.defaultHeader("User-Agent", "starter-client/1.0");
    return builder.build();
  }

  private boolean isMockServerUrl(String url) {
    return url.contains("localhost:1080") || url.contains("127.0.0.1:1080");
  }

  private static ExchangeFilterFunction logExchange() {
    int maxLogLength = 300; // TODO: move it to configuration via owner
    return ExchangeFilterFunction.ofRequestProcessor(request ->
        Mono.deferContextual(ctx -> {
          StringBuilder sb = new StringBuilder();
          sb.append("\n==================== [HTTP Request] ====================\n");
          sb.append("> ").append(request.method()).append(" ").append(request.url()).append("\n");
          request.headers().forEach((key, values) ->
              values.forEach(val -> sb.append("> ").append(key).append(": ").append(val).append("\n"))
          );
          return Mono.just(request).contextWrite(c -> c.put("requestLog", sb));
        })
    ).andThen(ExchangeFilterFunction.ofResponseProcessor(response ->
        Mono.deferContextual(ctx -> captureBodyWithSize(response)
            .flatMap(captured -> {
              BodyWithSize body = captured.body();
              StringBuilder sb = ctx.getOrDefault("requestLog", new StringBuilder());
              sb.append("==================== [HTTP Response] ===================\n");
              sb.append("< Status: ").append(captured.response().statusCode().value()).append("\n");
              captured.response().headers().asHttpHeaders().forEach((key, values) ->
                  values.forEach(val -> sb.append("< ").append(key).append(": ").append(val).append("\n"))
              );
              sb.append("--------------------------------------------------------\n");
              sb.append("Body size: ").append(body.size()).append(" bytes\n");
              sb.append(truncateBody(body.content(), maxLogLength)).append("\n");
              sb.append("========================================================");
              log.debug(sb.toString());
              return Mono.just(captured.response());
            })
        )
    ));
  }

  private static Mono<CapturedResponse<BodyWithSize>> captureBodyWithSize(ClientResponse response) {
    return response.bodyToFlux(DataBuffer.class)
        .map(dataBuffer -> {
          byte[] bytes = new byte[dataBuffer.readableByteCount()];
          dataBuffer.read(bytes);
          DataBufferUtils.release(dataBuffer);
          return bytes;
        })
        .collectList()
        .map(byteArrays -> {
          int totalSize = byteArrays.stream().mapToInt(b -> b.length).sum();
          String bodyStr = byteArrays.stream()
              .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
              .reduce("", String::concat);
          byte[] allBytes = byteArrays.stream()
              .reduce(new byte[0], (acc, bytes) -> {
                byte[] merged = new byte[acc.length + bytes.length];
                System.arraycopy(acc, 0, merged, 0, acc.length);
                System.arraycopy(bytes, 0, merged, acc.length, bytes.length);
                return merged;
              });
          DataBuffer buffer = new DefaultDataBufferFactory().wrap(allBytes);
          ClientResponse mutated = response.mutate().body(Flux.just(buffer)).build();
          return new CapturedResponse<>(mutated, new BodyWithSize(bodyStr, totalSize));
        });
  }

  private static String truncateBody(String body, int maxLength) {
    if (body == null) return "";
    if (body.length() <= maxLength) return body;
    return body.substring(0, maxLength) + "... (truncated)";
  }
}

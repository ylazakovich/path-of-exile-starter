package io.starter.client;

import java.net.URI;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

public class RequestExecutor {

  private final WebClient client;

  public RequestExecutor(WebClient client) {
    this.client = client;
  }

  public <T> Mono<ResponseEntity<T>> get(String path,
                                         Map<String, String> queryParams,
                                         Map<String, String> headers,
                                         ParameterizedTypeReference<T> responseType) {

    return client.get()
        .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams))
        .headers(httpHeaders -> headers.forEach(httpHeaders::set))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(responseType);
  }

  private URI buildUri(UriBuilder uriBuilder, String path, Map<String, String> queryParams) {
    UriBuilder builder = uriBuilder.path(path);
    queryParams.forEach(builder::queryParam);
    return URI.create(builder.build().toString());
  }
}

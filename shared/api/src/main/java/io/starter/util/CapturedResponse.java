package io.starter.util;

import org.springframework.web.reactive.function.client.ClientResponse;

public record CapturedResponse<T>(ClientResponse response, T body) {

}

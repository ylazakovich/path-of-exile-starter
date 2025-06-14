package io.starter.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

/**
 * Utils for creating and editing url entity.
 */
@UtilityClass
public class UrlUtils {

  /**
   * Add query parameters to url path.
   *
   * <p>ex: 'https://example.com' -> 'https://example.com?id=555'</p>
   * <p>ex: 'https://example.com' -> 'https://example.com?id=555&status[]=yes&status[]=no'</p>
   *
   * @param path        url path prefix to modified
   * @param queryParams parameters to add to url
   * @return modified url path with query parameters
   */
  @SneakyThrows(URISyntaxException.class)
  public String addQueryParameters(String path, Map<String, Object> queryParams) {
    List<NameValuePair> parameters = new ArrayList<>();
    queryParams.forEach((key, value) -> {
      if (Objects.requireNonNull(value) instanceof List<?> list) {
        list.forEach(arrayElement ->
            parameters.add(new BasicNameValuePair(key.concat("[]"), String.valueOf(arrayElement))));
      } else {
        parameters.add(new BasicNameValuePair(key, String.valueOf(value)));
      }
    });
    return new URIBuilder(path).addParameters(parameters).build().toString();
  }

  /**
   * Convert url {@link String} object to {@link URL} object.
   *
   * @param url url path to convert
   * @return {@link URL} class object
   */
  @SneakyThrows(MalformedURLException.class)
  public URL convertToUrl(String url) {
    return URI.create(url).toURL();
  }

  /**
   * Get page url with credentials.
   *
   * @param fullPageUrl full page url as {@link String}
   * @param login       login
   * @param password    password
   * @return page url with credentials
   */
  @SneakyThrows(URISyntaxException.class)
  public String getPageUrlWithCredentials(String fullPageUrl, String login, String password) {
    return new URIBuilder(fullPageUrl)
        .setUserInfo(String.format("%s:%s", login, password))
        .toString();
  }
}

package com.navneet.trade.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RestUtils {

  /**
   * This method makes a REST GET call to the specified URL with given headers and parameters.
   *
   * @param url     : Base url
   * @param headers : HTTP Headers map (key, value)
   * @param params  : Query parameters map (key, value)
   * @return : ResponseEntity with response body and status code
   */
  public ResponseEntity<String> restGetCall(
      String url, Map<String, String> headers, Map<String, String> params) {
    log.info("GET API Request received for url: {}, headers: REDACTED, params: {}", url, params);

    try (CloseableHttpClient restClient = createHttpClient()) {
      try {
        String urlWithParams = addParams(url, params);
        HttpGet getRequest = new HttpGet(urlWithParams);
        if (!CollectionUtils.isEmpty(headers)) {
          headers.forEach(
              (key, value) -> {
                if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
                  getRequest.addHeader(key, value);
                }
              });
        }

        var response = restClient.execute(getRequest);
        var entity = response.getEntity();
        HttpHeaders responseHeaders = new HttpHeaders();
        Arrays.stream(response.getHeaders())
            .forEach(header -> responseHeaders.add(header.getName(), header.getValue()));
        if (entity != null) {
          String responseBody = new String(entity.getContent().readAllBytes());
          log.info(
              "GET API Response received with status: {}, body: {}",
              response.getCode(),
              responseBody);
          return ResponseEntity.status(response.getCode())
              .headers(responseHeaders)
              .body(responseBody);
        } else {
          log.warn("GET API Response received with status: {}, but no content", response.getCode());
          return ResponseEntity.status(response.getCode()).headers(responseHeaders).body("");
        }
      } catch (IOException e) {
        log.error("IOException occurred during GET API call: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
      }
    } catch (IOException e) {
      log.error("Failed to close HttpClient: {}", e.getMessage());
    }
    // TODO Handle better
    return null;
  }

  public ResponseEntity<String> restPostCall(
      String url, Map<String, String> headers, Map<String, String> params, Object body) {
    log.info(
        "POST API Request received for url: {}, headers: {}, params: {}, body:{}", url, headers, params, body);
    try (CloseableHttpClient restClient = createHttpClient()) {
      try {
        String urlWithParams = addParams(url, params);
        HttpPost postRequest = new HttpPost(urlWithParams);
        if (!CollectionUtils.isEmpty(headers)) {
          headers.forEach(
              (key, value) -> {
                if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
                  postRequest.addHeader(key, value);
                }
              });
        }
        // Set body if present
        if (body != null) {
          postRequest.setEntity(
              new StringEntity(
                  new ObjectMapper().writeValueAsString(body), StandardCharsets.UTF_8));
        }
        var response = restClient.execute(postRequest);
        var entity = response.getEntity();
        HttpHeaders responseHeaders = new HttpHeaders();
        Arrays.stream(response.getHeaders())
            .forEach(header -> responseHeaders.add(header.getName(), header.getValue()));
        if (entity != null) {
          String responseBody = new String(entity.getContent().readAllBytes());
          log.info(
              "POST API Response received with status: {}, body: {}",
              response.getCode(),
              responseBody);

          return ResponseEntity.status(response.getCode())
              .headers(responseHeaders)
              .body(responseBody);
        } else {
          log.warn(
              "POST API Response received with status: {}, but no content", response.getCode());
          return ResponseEntity.status(response.getCode()).headers(responseHeaders).body("");
        }
      } catch (IOException e) {
        log.error("IOException occurred during POST API call: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
      }
    } catch (IOException e) {
      log.error("Failed to close HttpClient: {}", e.getMessage());
    }
    // TODO Handle better
    return null;
  }

  /**
   * Add query parameters to the URL
   *
   * @param url    : Base URL
   * @param params : Params map (key, value)
   * @return : Updated query
   */
  protected String addParams(String url, Map<String, String> params) {
    if (CollectionUtils.isEmpty(params)) {
      return url;
    }
    StringBuilder urlWithParams = new StringBuilder(url).append("?");
    params.forEach(
        (key, value) -> {
          if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
            urlWithParams
                .append(key)
                .append("=")
                .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                .append("&");
          }
        });
    // Remove the trailing '&' if present
    if (urlWithParams.charAt(urlWithParams.length() - 1) == '&') {
      urlWithParams.deleteCharAt(urlWithParams.length() - 1);
    }
    return urlWithParams.toString();
  }

  /**
   * Factory method to create CloseableHttpClient. Extracted to allow tests to inject mocks.
   *
   * @return CloseableHttpClient instance
   */
  protected CloseableHttpClient createHttpClient() {
    return HttpClients.createDefault();
  }
}

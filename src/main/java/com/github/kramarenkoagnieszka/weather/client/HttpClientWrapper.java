package com.github.kramarenkoagnieszka.weather.client;

import com.github.kramarenkoagnieszka.weather.app.AppConfig;
import com.github.kramarenkoagnieszka.weather.exception.HttpClientWrapperException;
import com.github.kramarenkoagnieszka.weather.exception.HttpServerException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
public class HttpClientWrapper {

  private final HttpClient httpClient;

  public HttpResponse<String> sendWithRetry(HttpRequest request) {
    int maxRetries = AppConfig.DEFAULT_RETRIES;

    HttpRequest timedRequest = HttpRequest.newBuilder(request.uri())
        .timeout(Duration.ofSeconds(AppConfig.HTTP_READ_TIMEOUT_SECONDS))
        .method(
            request.method(),
            request.bodyPublisher().orElse(HttpRequest.BodyPublishers.noBody())
        )
        .build();

    int attempt = 0;
    Exception lastException = null;

    while (attempt < maxRetries) {
      try {
        HttpResponse<String> response = httpClient.send(timedRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 500) {
          lastException = new HttpServerException("Server returned status 500");
        } else {
          return response;
        }

      } catch (IOException e) {
        lastException = e;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new HttpClientWrapperException("HTTP request execution was interrupted", e);
      }
      attempt++;
    }

    throw new HttpClientWrapperException(
        String.format("HTTP request failed after %d attempts", maxRetries),
        lastException
    );
  }
}

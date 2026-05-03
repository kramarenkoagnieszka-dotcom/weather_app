package com.github.kramarenkoagnieszka.weather.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpClientWrapperTest {

  @Mock
  private HttpClient httpClient;

  @Mock
  private HttpResponse<String> httpResponse;

  @InjectMocks
  private HttpClientWrapper httpClientWrapper;

  @Test
  @DisplayName("Should return response immediately on first successful attempt")
  void shouldReturnResponseOnFirstAttempt() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://test.com")).build();
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);

    HttpResponse<String> result = httpClientWrapper.sendWithRetry(request, 3);

    assertThat(result).isEqualTo(httpResponse);
    verify(httpClient, times(1)).send(any(), any()); // Sprawdzamy, czy wywołano tylko raz
  }

  @Test
  @DisplayName("Should retry and succeed on second attempt after one IOException")
  void shouldRetryAndSucceedOnSecondAttempt() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://test.com")).build();

    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenThrow(new IOException("Network down"))
        .thenReturn(httpResponse);

    HttpResponse<String> result = httpClientWrapper.sendWithRetry(request, 3);

    assertThat(result).isEqualTo(httpResponse);
    verify(httpClient, times(2)).send(any(), any()); // Potwierdzamy, że był retry!
  }

  @Test
  @DisplayName("Should throw WeatherClientException after exhausting all retries")
  void shouldFailAfterAllRetries() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://test.com")).build();
    int maxRetries = 3;

    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenThrow(new IOException("Permanent failure"));

    assertThatThrownBy(() -> httpClientWrapper.sendWithRetry(request, maxRetries))
        .isInstanceOf(WeatherClientException.class)
        .hasMessageContaining("failed after 3 attempts");

    verify(httpClient, times(maxRetries)).send(any(), any());
  }
}

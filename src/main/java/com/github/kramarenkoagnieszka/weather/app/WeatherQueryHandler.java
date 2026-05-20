package com.github.kramarenkoagnieszka.weather.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kramarenkoagnieszka.weather.exception.GeocodingClientException;
import com.github.kramarenkoagnieszka.weather.exception.GeocodingUpstreamException;
import com.github.kramarenkoagnieszka.weather.exception.HttpClientWrapperException;
import com.github.kramarenkoagnieszka.weather.exception.MissingParameterException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherApplicationException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherUpstreamException;
import com.github.kramarenkoagnieszka.weather.model.WeatherRequest;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;

import java.util.Collections;
import java.util.Map;

public class WeatherQueryHandler
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final ApplicationContext APP_CONTEXT = new ApplicationContext();

  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context awsContext) {
    if (awsContext != null) {
      awsContext.getLogger().log("Processing weather request...");
    }

    try {
      Map<String, String> queryParams = extractQueryParams(input);
      validateQueryParams(queryParams);

      String queryValue = queryParams.get(AppConfig.QUERY_PARAM);
      WeatherResponse weatherResponse =
          APP_CONTEXT.getWeatherService().getWeather(new WeatherRequest(queryValue));

      return buildResponse(200, weatherResponse);

    } catch (MissingParameterException e) {
      logError(awsContext, "Validation error: ", e);
      return buildErrorResponse(400, "Bad Request: " + e.getMessage());
    } catch (GeocodingClientException | WeatherClientException e) {
      logError(awsContext, "Client error: ", e);
      return buildErrorResponse(404, "Not Found: " + e.getMessage());
    } catch (HttpClientWrapperException e) {
      logError(awsContext, "Upstream timeout: ", e);
      return buildErrorResponse(504, "Gateway Timeout: " + e.getMessage());
    } catch (GeocodingUpstreamException | WeatherUpstreamException e) {
      logError(awsContext, "Upstream error: ", e);
      return buildErrorResponse(502, "Bad Gateway: " + e.getMessage());
    } catch (WeatherApplicationException e) {
      logError(awsContext, "Service error: ", e);
      return buildErrorResponse(500, "Service Error: " + e.getMessage());
    } catch (Exception e) {
      logError(awsContext, "Fatal error: ", e);
      return buildErrorResponse(500, "Internal Server Error");
    }
  }

  private Map<String, String> extractQueryParams(APIGatewayProxyRequestEvent input) {
    if (input == null) {
      throw new MissingParameterException("Input cannot be null");
    }
    Map<String, String> params = input.getQueryStringParameters();
    return (params != null) ? params : Collections.emptyMap();
  }

  private void validateQueryParams(Map<String, String> queryParams) {
    String value = queryParams.get(AppConfig.QUERY_PARAM);
    if (value == null || value.isBlank()) {
      throw new MissingParameterException(
          String.format("Missing required parameter: '%s'", AppConfig.QUERY_PARAM));
    }
  }

  private APIGatewayProxyResponseEvent buildResponse(int statusCode, Object body) {
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
    response.setStatusCode(statusCode);
    response.setHeaders(Map.of("Content-Type", "application/json"));
    try {
      response.setBody(APP_CONTEXT.getObjectMapper().writeValueAsString(body));
    } catch (JsonProcessingException e) {
      response.setStatusCode(500);
      response.setBody("{\"error\":\"Failed to serialize response\"}");
    }
    return response;
  }

  private APIGatewayProxyResponseEvent buildErrorResponse(int statusCode, String message) {
    return buildResponse(statusCode, Map.of(
        "errorCode", statusCode,
        "message", message
    ));
  }

  private void logError(Context context, String message, Exception e) {
    if (context != null) {
      context.getLogger().log(message + e.getMessage());
    }
  }
}

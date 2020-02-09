package ca.atb.hackathon_2020;


import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RestClient {
    private RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(RestClient.class);

    public RestClient() {
        restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<List<T>> find(String url, HttpHeaders headers, Map<String, ?> params, Class<T> type) {
        ParameterizedType listType = TypeUtils.parameterize(List.class, type);

        return httpCall(HttpMethod.GET, url, headers, params, ParameterizedTypeReference.forType(listType));
    }

    public <T> ResponseEntity<List<T>> find(String templateUrl, Map<String, ?> pathVariables, HttpHeaders headers, Map<String, ?> params, Class<T> type) {
        ParameterizedType listType = TypeUtils.parameterize(List.class, type);

        URI uriWithTemplate = getUriWithTemplate(templateUrl, pathVariables);

        return httpCall(HttpMethod.GET, uriWithTemplate, headers, null, ParameterizedTypeReference.forType(listType));
    }

    public <T> ResponseEntity<List<T>> get(String url, HttpHeaders headers, Map<String, ?> params, Class<T> type) {

        return httpCall(HttpMethod.GET, url, headers, params, ParameterizedTypeReference.forType(type));
    }

    public <T> Optional<T> get(String templateUrl, Map<String, ?> pathVariables, HttpHeaders headers, Map<String, ?> params, Class<T> type) {
        URI uriWithTemplate = getUriWithTemplate(templateUrl, pathVariables);

        try {
            ResponseEntity<T> response = httpCall(HttpMethod.GET, uriWithTemplate, headers, null, ParameterizedTypeReference.forType(type));
            if (response.getBody() == null) {
                return Optional.empty();
            } else {
                return Optional.of(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            return commonErrorHandler(e, HttpMethod.PUT);
        }
    }

    public <T> Optional<T> post(String url, HttpHeaders headers, Object body, Class<T> type) {
        URI uri =  UriComponentsBuilder.fromUriString(url).build().toUri();
        return post(uri, headers, body, type);
    }

    public <T> Optional<T> post(String templateUrl, Map<String, ?> pathVariables, HttpHeaders headers, Object body, Class<T> type) {
        URI uriWithTemplate = getUriWithTemplate(templateUrl, pathVariables);
        return post(uriWithTemplate, headers, body, type);
    }

    private <T> Optional<T> post(URI uri, HttpHeaders headers, Object body, Class<T> type) {
        try {
            ResponseEntity<T> response = httpCall(HttpMethod.POST, uri, headers, body, ParameterizedTypeReference.forType(type));
            if (response.getBody() == null) {
                return Optional.empty();
            } else {
                return Optional.of(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            return commonErrorHandler(e, HttpMethod.PUT);
        }
    }

    public <T> Optional<T> put(String templateUrl, Map<String, ?> pathVariables, HttpHeaders headers, Object body, Class<T> type) {
        URI uriWithTemplate = getUriWithTemplate(templateUrl, pathVariables);
        return put(uriWithTemplate, headers, body, type);
    }

    public <T> Optional<T> put(String url, HttpHeaders headers, Object body, Class<T> type) {
        URI uri =  UriComponentsBuilder.fromUriString(url).build().toUri();
        return put(uri, headers, body, type);
    }

    public <T> boolean put(String templateUrl, Map<String, ?> pathVariables, HttpHeaders headers, Class<T> type) {
        URI uriWithTemplate = getUriWithTemplate(templateUrl, pathVariables);
        return put(uriWithTemplate, headers, type);
    }

    public <T> boolean put(String url, HttpHeaders headers, Class<T> type) {
        URI uri =  UriComponentsBuilder.fromUriString(url).build().toUri();
        return put(uri, headers, type);
    }

    private <T> boolean put(URI uri, HttpHeaders headers, Class<T> type) {
        try {
            ResponseEntity<T> response = httpCall(HttpMethod.PUT, uri, headers, null, ParameterizedTypeReference.forType(type));
            return response.getStatusCode().equals(HttpStatus.OK) || response.getStatusCode().equals(HttpStatus.NO_CONTENT);
        } catch (HttpClientErrorException e) {
            commonErrorHandler(e, HttpMethod.PUT);
            return false;
        }
    }

    private <T> Optional<T> put(URI uri, HttpHeaders headers, Object body, Class<T> type) {
        try {
            ResponseEntity<T> response = httpCall(HttpMethod.PUT, uri, headers, body, ParameterizedTypeReference.forType(type));
            if (response.getBody() == null) {
                return Optional.empty();
            } else {
                return Optional.of(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            return commonErrorHandler(e, HttpMethod.PUT);
        }
    }

    public <T> boolean delete(String templateUrl, Map<String, ?> pathVariables, HttpHeaders headers, Class<T> type) {
        URI uriWithTemplate = getUriWithTemplate(templateUrl, pathVariables);
        return delete(uriWithTemplate, headers, type);
    }

    public <T> boolean delete(String url, HttpHeaders headers, Class<T> type) {
        URI uri =  UriComponentsBuilder.fromUriString(url).build().toUri();
        return delete(uri, headers, type);
    }

    private <T> boolean delete(URI uri, HttpHeaders headers, Class<T> type) {
        try {
            ResponseEntity<T> response = httpCall(HttpMethod.DELETE, uri, headers, null, ParameterizedTypeReference.forType(type));
            return response.getStatusCode().equals(HttpStatus.OK) || response.getStatusCode().equals(HttpStatus.NO_CONTENT);
        } catch (HttpClientErrorException e) {
            commonErrorHandler(e, HttpMethod.DELETE);
            return false;
        }
    }

    public URI getUriWithTemplate(String template, Map<String, ?> pathVariables) {
        UriComponents components = UriComponentsBuilder.fromUriString(template).build();
        return components.expand(pathVariables).toUri();
    }


    public <T> ResponseEntity<T> httpCall(HttpMethod method, String url, HttpHeaders headers, Map<String, ?> params, ParameterizedTypeReference<T> ref) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        if (params != null) {
            params.forEach(builder::queryParam);
        }

        return httpCall(method, builder.build().toUri(), headers, null, ref);
    }

    public <T> ResponseEntity<T> httpCall(HttpMethod method, URI uri, HttpHeaders headers, Object body, ParameterizedTypeReference<T> ref) {
        try {
            log.debug("Making {} call to {}", method, uri);
            if (body != null) {
                log.debug("\twith body: {}", body);
            }

            ResponseEntity<T> exchange = restTemplate.exchange(
                    uri,
                    method,
                    new HttpEntity<>(body, headers), //headers
                    ref //type expected back
            );

            return exchange;
        } catch (HttpClientErrorException e) {
            log.error("Received Response body: {}", e.getResponseBodyAsString());
            throw e;
        }
    }


    private <T> Optional<T> commonErrorHandler(HttpClientErrorException e, HttpMethod method) {
        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            return Optional.empty();
        }
        throw e;
    }

}

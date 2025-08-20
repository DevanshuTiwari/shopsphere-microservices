package com.dt.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().filter(addJwtTokenFilter());
    }

    private ExchangeFilterFunction addJwtTokenFilter() {
        return (clientRequest, next) -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String tokenValue = null;

            // Reliably get the token from the credentials field
            if (authentication != null && authentication.getCredentials() instanceof String) {
                tokenValue = (String) authentication.getCredentials();
            }

            if (tokenValue != null) {
                ClientRequest authorizedRequest = ClientRequest.from(clientRequest)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                        .build();
                return next.exchange(authorizedRequest);
            }
            return next.exchange(clientRequest);
        };
    }
}

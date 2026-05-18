package com.studyconnect.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Configuración del API Gateway.
 * Define el KeyResolver para el rate limiter basado en la IP del cliente.
 */
@Configuration
public class GatewayConfig {

    /**
     * Resuelve la clave de rate limiting por dirección IP del cliente.
     * Necesario para que RequestRateLimiter funcione con peticiones anónimas.
     * Sin este bean, el gateway usa PrincipalNameKeyResolver que produce
     * claves vacías y devuelve 429 con cuerpo vacío para peticiones anónimas.
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = Objects.requireNonNullElse(
                    exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"),
                    Objects.requireNonNullElse(
                            exchange.getRequest().getRemoteAddress() != null
                                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                                    : null,
                            "unknown"
                    )
            );
            // Tomar solo la primera IP si hay múltiples (X-Forwarded-For puede ser lista)
            if (ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
            return Mono.just(ip);
        };
    }
}

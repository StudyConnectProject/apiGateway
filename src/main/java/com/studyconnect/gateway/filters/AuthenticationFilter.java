package com.studyconnect.gateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>
        implements Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    /**
     * Routes that do NOT require a JWT token.
     */
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh",
            "/api/auth/validate-token",
            "/api/auth/health",
            "/actuator/health",
            "/fallback"
    );

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly12345}")
    private String jwtSecret;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> filter(exchange, chain);
    }

    private Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("[AUTH] Missing or malformed Authorization header for path: {}", path);
            return unauthorized(exchange, "Missing Authorization header");
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = validateToken(token);
            String userId  = claims.getSubject();
            Object roles   = claims.get("roles");
            String email   = (String) claims.get("email");

            // Forward user context as headers to downstream microservices
            ServerWebExchange mutated = exchange.mutate()
                    .request(r -> r.header("X-User-Id",    userId)
                                   .header("X-User-Role",  roles  != null ? roles.toString()  : "")
                                   .header("X-User-Email", email  != null ? email : ""))
                    .build();

            return chain.filter(mutated);

        } catch (JwtException e) {
            log.warn("[AUTH] Invalid JWT token for path {}: {}", path, e.getMessage());
            return unauthorized(exchange, "Invalid or expired token");
        }
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private Claims validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = String.format(
                "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"status\":401}", message);
        var buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public static class Config {
        // No extra config needed for now
    }
}

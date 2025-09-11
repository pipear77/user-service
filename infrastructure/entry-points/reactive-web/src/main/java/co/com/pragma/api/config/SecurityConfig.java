package co.com.pragma.api.config;

import co.com.pragma.model.usuario.gateways.JwtProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProviderRepository jwtProvider;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()
                        .pathMatchers(HttpMethod.GET, "/auth/validate").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/validate").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/usuarios/**").authenticated() //Protege /usuarios/{documento}
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter jwtAuthenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtAuthenticationManager());
        filter.setServerAuthenticationConverter(jwtAuthenticationConverter());

        filter.setRequiresAuthenticationMatcher(exchange -> {
            String path = exchange.getRequest().getPath().value();
            boolean requiereAuth = path.startsWith("/api/") || path.startsWith("/auth/") || path.startsWith("/usuarios/");
            boolean esValidacion = path.equals("/auth/validate") || path.equals("/api/v1/validate");

            return requiereAuth && !esValidacion
                    ? ServerWebExchangeMatcher.MatchResult.match()
                    : ServerWebExchangeMatcher.MatchResult.notMatch();
        });

        return filter;
    }

    private ReactiveAuthenticationManager jwtAuthenticationManager() {
        return authentication -> {
            String token = authentication.getCredentials().toString();
            if (!jwtProvider.validateToken(token)) {
                return Mono.empty();
            }

            String correo = jwtProvider.getSubject(token);
            log.info("Token validado para usuario: {}", correo);
            String rol = jwtProvider.getClaim(token, "rol");
            log.info("Rol extraído del token: {}", rol);

            AbstractAuthenticationToken authToken = new AbstractAuthenticationToken(
                    List.of(new SimpleGrantedAuthority(rol))) {
                @Override
                public Object getCredentials() {
                    return token;
                }

                @Override
                public Object getPrincipal() {
                    return correo;
                }
            };
            authToken.setAuthenticated(true);
            return Mono.just(authToken);
        };
    }

    private ServerAuthenticationConverter jwtAuthenticationConverter() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .filter(auth -> auth.startsWith("Bearer "))
                .map(auth -> auth.substring(7))
                .map(token -> new AbstractAuthenticationToken(List.of()) {
                    @Override
                    public Object getCredentials() {
                        return token;
                    }

                    @Override
                    public Object getPrincipal() {
                        return null;
                    }
                });
    }
}

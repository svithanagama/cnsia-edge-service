package me.sanjayav.polarbookshop.cnsiaedgeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
      ReactiveClientRegistrationRepository clientRegistrationRepository) {
    return http
        .authorizeExchange(exchange -> exchange
            // allow static resources to be accessed anonymously
            .pathMatchers("/", "/*.css", "/*.js", "favicon.ico")
              .permitAll()
            // books should be viewable by everyone
            .pathMatchers(HttpMethod.GET, "/books/**")
              .permitAll()
            // everything else requires authentication
            .anyExchange().authenticated()
        )
        // return a 401 instead of redirecting to login page
        // as SPA will handle the redirection to login page
        .exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(
                new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)
            )
        )
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.logoutSuccessHandler(
            oidcLogoutSuccessHandler(clientRegistrationRepository)))
        .build();
  }

  private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(
      ReactiveClientRegistrationRepository clientRegistrationRepository
  ) {
    var oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
    oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
    return oidcLogoutSuccessHandler;
  }
}

package me.sanjayav.polarbookshop.cnsiaedgeservice.user;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

  @GetMapping("user")
  public Mono<User> getUser(
      @AuthenticationPrincipal OidcUser oidcUser
  ) {
    var user = new User(
        oidcUser.getPreferredUsername(),
        oidcUser.getGivenName(),
        oidcUser.getFamilyName(),
        // this is hardcoded temporarily
        List.of("employee", "customer")
    );
    return Mono.just(user);
  }
}
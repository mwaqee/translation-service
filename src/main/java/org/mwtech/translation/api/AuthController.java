package org.mwtech.translation.api;

import org.mwtech.translation.api.dto.LoginRequest;
import org.mwtech.translation.api.dto.TokenResponse;
import org.mwtech.translation.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final JwtUtil jwt;
  public AuthController(JwtUtil jwt){ this.jwt = jwt; }

  @PostMapping("/token")
  public ResponseEntity<TokenResponse> token(@RequestBody LoginRequest req){
    if ("admin@example.com".equalsIgnoreCase(req.email()) && "secret".equals(req.password())) {
      String t = jwt.generateToken(req.email());
      return ResponseEntity.ok(new TokenResponse(t));
    }
    return ResponseEntity.status(401).build();
  }
}

package org.mwtech.translation.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

  @Test
  void generate_and_parse_token() {
    JwtUtil util = new JwtUtil(
        "a-very-long-test-secret-for-hmac-sha256-1234567890-1234567890",
        "issuer", 10
    );
    String token = util.generateToken("user@example.com");
    var claims = util.parse(token).getBody();
    assertThat(claims.getSubject()).isEqualTo("user@example.com");
    assertThat(claims.getIssuer()).isEqualTo("issuer");
  }
}

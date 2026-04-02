package com.gerenciamento_hospitalar.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    @Value(value = "${spring.security.jwt.secret-key}")
    private String secretKey;

    @Value(value = "${spring.security.jwt.expire-length}")
    private long expireLength;

    private final UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAcessToken(String username, List<String> roles) {

        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expireLength);
        String acessToken = getAcessToken(username, roles, createdDate, expirationDate);
        String refreshToken = getRefreshToken(username, roles, createdDate);
        
        return new TokenDTO(username, true, createdDate, expirationDate, acessToken, refreshToken);
    }

    public TokenDTO createRefreshToken(String refreshtoken) {

        String token = "";
        if(StringUtils.isEmpty(refreshtoken) && refreshtoken.startsWith("Bearer ")) {
            token = refreshtoken.substring("Bearer ".length());
        }

        DecodedJWT decodedJWT = getDecodedJWT(token);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

        return createAcessToken(username, roles);
    }



    private String getAcessToken(String username, List<String> roles, Date createdDate, Date expirationDate) {
        String issuerURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(createdDate)
                .withIssuer(issuerURL)
                .withExpiresAt(expirationDate)
                .withSubject(username)
                .sign(algorithm);
    }

    private String getRefreshToken(String username, List<String> roles, Date createdDate) {

        Date expirationDate = new Date(createdDate.getTime() + (expireLength * 3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(createdDate)
                .withExpiresAt(expirationDate)
                .withSubject(username)
                .sign(algorithm);
    }

    // Obtém um objeto authentication através do payload do token
    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT getDecodedJWT(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT;
    }

    // obtém o token pelo header da requisição.
    public String resolveToken(HttpServletRequest request) {
        String authentication = request.getHeader("Authentication");

        if(!StringUtils.isEmpty(authentication) && authentication.startsWith("Bearer ")) {
            return authentication.substring("Bearer ".length());
        }
        return null;
    }

    // verifica se o token já foi expirado.
    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);

        try {
            if(decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            else {
                return true;
            }
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT token.");
        }
    }
}

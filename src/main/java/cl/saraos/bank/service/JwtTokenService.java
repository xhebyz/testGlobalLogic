package cl.saraos.bank.service;

import cl.saraos.bank.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Date;
import java.util.Objects;

@Service
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        long expirationTimeMillis = System.currentTimeMillis() + 3600000; // 1 hora de validez
        Date expirationDate = new Date(expirationTimeMillis);

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public boolean validateToken(String token) {
        return Objects.nonNull(this.getTokenClaims(token));
    }
    public Jws<Claims> getTokenClaims(String token){
        try {
            // Valida el token usando la clave secreta
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claimsJws;
        } catch (JwtException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }
}

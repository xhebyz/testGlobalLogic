package cl.saraos.bank.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Date;

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

    public boolean validateToken(String token){
        try {
            // Valida el token usando la clave secreta
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }
}

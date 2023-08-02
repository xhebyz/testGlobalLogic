package cl.saraos.bank.filter;

import cl.saraos.bank.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String SECRET_KEY;

    private final HandlerExceptionResolver resolver;

    public JwtTokenFilter(@Value("${jwt.secret}") String secretKey,
                          HandlerExceptionResolver resolver) {
        this.SECRET_KEY = secretKey;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

                // Aquí puedes hacer más validaciones con las claims si lo deseas, como verificar roles o permisos.
                request.setAttribute("claims", claims);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            resolver.resolveException(request, response, null, new UnauthorizedException("Token inválido o expirado"));
        }
    }

}

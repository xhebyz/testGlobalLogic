package cl.saraos.bank.aspect;

import cl.saraos.bank.domain.login.LoginRequest;
import cl.saraos.bank.exceptions.UnauthorizedException;
import cl.saraos.bank.service.JwtTokenService;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class AuthTokenAspect {

    private static JwtTokenService tokenService;

    @Autowired
    public AuthTokenAspect(JwtTokenService tokenService) {
        AuthTokenAspect.tokenService = tokenService;
    }

    @Before("@within(cl.saraos.bank.aop.AuthToken) || @annotation(cl.saraos.bank.aop.AuthToken)")
    public void authenticate(JoinPoint joinPoint) {

        LoginRequest request = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof LoginRequest)
                .map(arg -> (LoginRequest) arg)
                .findFirst()
                .orElse(null);

        boolean validate = false;
        if (Objects.nonNull(request) && Objects.nonNull(request.getToken())) {
            validate = tokenService.validateToken(request.getToken());
        }
        if (!validate) {
            // Si el token no es válido, lanza una excepción o realiza la acción deseada.
            throw new UnauthorizedException("Token JWT no válido.");
        }


    }
}

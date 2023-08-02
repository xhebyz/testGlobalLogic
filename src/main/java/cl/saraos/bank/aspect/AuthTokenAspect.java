package cl.saraos.bank.aspect;

import cl.saraos.bank.domain.login.LoginRequest;
import cl.saraos.bank.exceptions.UnauthorizedException;
import cl.saraos.bank.service.JwtTokenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class AuthTokenAspect {

    private static JwtTokenService tokenService;

    @Autowired
    public AuthTokenAspect(JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Before("@within(cl.saraos.bank.aop.AuthToken) || @annotation(cl.saraos.bank.aop.AuthToken)")
    public void authenticate(JoinPoint joinPoint) throws Throwable{

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

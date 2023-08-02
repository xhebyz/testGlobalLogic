package cl.saraos.bank.aspect;

import cl.saraos.bank.service.JwtTokenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

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


        Object[] args = joinPoint.getArgs();

        HttpServletRequest request = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof HttpServletRequest)
                .map(arg -> (HttpServletRequest) arg)
                .findFirst()
                .orElse(null);

        if (request != null) {
            String requestBody = (String) request.getAttribute("requestBodyData");
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody,
                    new TypeReference<Map<String, Object>>() {});
            String token = (String) requestBodyMap.get("token");
            boolean validate = tokenService.validateToken(token);

            if (!validate) {
                // Si el token no es válido, lanza una excepción o realiza la acción deseada.
                throw new RuntimeException("Token JWT no válido.");
            }
        }

    }
}

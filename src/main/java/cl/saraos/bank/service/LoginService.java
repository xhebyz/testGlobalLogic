package cl.saraos.bank.service;

import cl.saraos.bank.domain.commons.Phone;
import cl.saraos.bank.domain.login.LoginRequest;
import cl.saraos.bank.domain.login.LoginResponse;
import cl.saraos.bank.domain.register.RegisterResponse;
import cl.saraos.bank.entity.UserEntity;
import cl.saraos.bank.exceptions.UnauthorizedException;
import cl.saraos.bank.exceptions.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;


    @Autowired
    public LoginService(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    public LoginResponse login(LoginRequest request){
        String token = request.getToken();
        Jws<Claims> claimsJws = jwtTokenService.getTokenClaims(token);
        String email = claimsJws.getBody().getSubject();
        UserEntity userSaved = userService.getUser(email);

        if(Objects.isNull(userSaved)){
            throw new UserNotFoundException("Usuario no existe");
        }

        //save login date
        userSaved.setLastLogin(new Date());
        userService.updateUser(userSaved);

        return LoginResponse.builder()
                .isActive(userSaved.getIsActive())
                .token(jwtTokenService.generateToken(userSaved.getEmail()))
                .id(userSaved.getId())
                .name(userSaved.getName())
                .email(userSaved.getEmail())
                .password(userSaved.getPassword())
                .lastLogin(userSaved.getLastLogin())
                .created(userSaved.getCreatedAt())
                .phones(userSaved.getPhones().stream().map(
                        phoneEntity -> Phone.builder()
                                .number(phoneEntity.getNumber())
                                .citycode(phoneEntity.getCitycode())
                                .contrycode(phoneEntity.getContrycode())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }
}

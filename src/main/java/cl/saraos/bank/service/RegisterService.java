package cl.saraos.bank.service;

import cl.saraos.bank.domain.commons.Phone;
import cl.saraos.bank.domain.register.RegisterRequest;
import cl.saraos.bank.domain.register.RegisterResponse;
import cl.saraos.bank.entity.PhoneEntity;
import cl.saraos.bank.entity.UserEntity;
import cl.saraos.bank.exceptions.UserExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RegisterService {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public RegisterService(PasswordEncoder passwordEncoder, UserService userService, JwtTokenService jwtTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    public RegisterResponse saveUser(RegisterRequest request) {

        String email = request.getEmail();
        UserEntity user =  userService.getUser(email);
        if(Objects.nonNull(user)){
            //existe
            throw new UserExistException("Usuario existe");
        }
        String pass = request.getPassword();
        // Generar un salt aleatorio
        String salt = BCrypt.gensalt();
        // Aplicar el hash de Bcrypt con el salt generado
        String hashedPassword = passwordEncoder.encode(pass + salt);


        user = UserEntity.builder().name(request.getName()).password(hashedPassword).email(request.getEmail())
                .lastLogin(new Date())
                .isActive(true).build();
        UserEntity finalUser = user;
        List<PhoneEntity> phonesList = request.getPhones().stream().map(phone ->
                PhoneEntity.builder().number(phone.getNumber()).user(finalUser)
                        .citycode(phone.getCitycode()).contrycode(phone.getContrycode())
                        .build()).collect(Collectors.toList()
        );

        user.setPhones(
                phonesList
        );

        UserEntity userSaved = userService.saveUser(user);
        return RegisterResponse.builder()
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

    public boolean validatePassword(String password, String validPassword, String salt) {
        String hashedPassword = passwordEncoder.encode(password + salt);
        return hashedPassword.equals(validPassword);
    }
}

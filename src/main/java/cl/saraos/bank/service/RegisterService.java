package cl.saraos.bank.service;

import cl.saraos.bank.domain.register.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(RegisterRequest request) {

        String email = request.getEmail();
        String pass = request.getPassword();

        // Generar un salt aleatorio
        String salt = BCrypt.gensalt();

        // Aplicar el hash de Bcrypt con el salt generado
        String hashedPassword = passwordEncoder.encode(pass + salt);

    }

    public boolean validatePassword(String password, String validPassword, String salt) {
        String hashedPassword = passwordEncoder.encode(password + salt);
        return hashedPassword.equals(validPassword);
    }
}

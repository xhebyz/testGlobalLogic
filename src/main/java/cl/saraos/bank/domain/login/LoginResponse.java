package cl.saraos.bank.domain.login;

import cl.saraos.bank.domain.commons.Phones;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String id;
    private String created;
    private String lastLogin;
    private String token;
    private boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<Phones> phones;
}

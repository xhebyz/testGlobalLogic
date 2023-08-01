package cl.saraos.bank.domain.register;

import lombok.Data;

@Data
public class RegisterResponse {

    private String id;
    private long created;
    private long lastLogin;
    private String token;
    private boolean isActive;

}

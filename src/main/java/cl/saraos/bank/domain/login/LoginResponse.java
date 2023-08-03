package cl.saraos.bank.domain.login;

import cl.saraos.bank.domain.commons.Phone;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date created;
    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date lastLogin;
    private String token;
    private boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
}

package cl.saraos.bank.domain.register;

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
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Long id;

    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date created;

    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date lastLogin;

    private String token;
    private Boolean isActive;
    private String name;
    private String email;
    private String password;

    private List<Phone> phones;

}

package cl.saraos.bank.domain.register;

import cl.saraos.bank.domain.commons.Phone;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    private String name;

    @Email(message = "El correo debe ser una dirección valida")
    @NotNull(message = "El correo no puede ser nulo")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d.*\\d)(?!.*\\s)(?!.*[a-z]{13,})(?!.*[A-Z]{2,})(?!.*\\d{3,})(?!.*\\W).{8," +
            "12}$", message = "La clave no cumple con el formato requerido.")
    @NotNull(message = "La contraseña no puede ser nula")
    private String password;

    private List<Phone> phones;
}

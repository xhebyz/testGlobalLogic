package cl.saraos.bank.domain.register;

import cl.saraos.bank.domain.commons.Phone;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;

    @Email(message = "El correo debe ser una dirección valida")
    @NotNull(message = "El correo no puede ser nulo")
    private String email;

    @Pattern(regexp = "^(?=(?:[^A-Z]*[A-Z]){1})(?!.*[A-Z].*[A-Z])(?=(?:\\D*\\d){2})(?!.*\\d.*\\d.*\\d)[A-Za-z0-9]{8,12}$"
            , message = "La clave no cumple con el formato requerido.")
    @NotNull(message = "La contraseña no puede ser nula")
    private String password;

    private List<Phone> phones;
}

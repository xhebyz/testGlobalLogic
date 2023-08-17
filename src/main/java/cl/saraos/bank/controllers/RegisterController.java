package cl.saraos.bank.controllers;

import cl.saraos.bank.domain.register.RegisterRequest;
import cl.saraos.bank.domain.register.RegisterResponse;
import cl.saraos.bank.service.RegisterService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping(value = "/sign-up")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponse> signUp(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(registerService.saveUser(registerRequest));
    }

}

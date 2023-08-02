package cl.saraos.bank.controllers;

import cl.saraos.bank.aop.AuthToken;
import cl.saraos.bank.domain.login.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AuthToken
@RequestMapping(value = "/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok("OK.");
    }
}

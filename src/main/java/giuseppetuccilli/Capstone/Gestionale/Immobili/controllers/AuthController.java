package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.EmailValidationPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.LoginRequest;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.RegistUtentePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.ResetPasswordPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.LoginResponse;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.UtenteResponsePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.AuthService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder bCrypt;
    @Autowired
    private EmailService emailService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteResponsePayload register(@RequestBody @Validated RegistUtentePayload payload, BindingResult br) {
        if (br.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < br.getFieldErrors().size(); i++) {
                errList.add(br.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        RegistUtentePayload utToSave = new RegistUtentePayload(payload.nome(), payload.cognome(), payload.email(), bCrypt.encode(payload.password()), payload.telefono());
        Utente u = authService.salvaAdmin(utToSave);
        emailService.addToMailgun(u.getEmail());
        return new UtenteResponsePayload(u.getId(), u.getNome(), u.getCognome());

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest body, BindingResult br) {
        if (br.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < br.getFieldErrors().size(); i++) {
                errList.add(br.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        String token = authService.checkAndGenerate(body);
        return new LoginResponse(token);
    }

    //reset della password dimenticata
    //1) invio codice per email
    @GetMapping("/richiediCodice")
    @ResponseStatus(HttpStatus.CREATED)
    public void inviaCodice(@RequestBody EmailValidationPayload body) {
        String email = body.email();
        authService.inviaCodice(email);
    }

    //2) verifica codice e reset password
    @PatchMapping("/resetPassword")
    @ResponseStatus(HttpStatus.CREATED)
    public void VerificaAndReset(@RequestBody ResetPasswordPayload body) {
        String newPassword = body.newPassword();
        String codice = body.codice();
        authService.verificaAndResetta(codice, newPassword);
    }

}

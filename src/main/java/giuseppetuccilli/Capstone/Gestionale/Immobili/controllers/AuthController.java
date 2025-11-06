package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.LoginRequest;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.RegistUtentePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.LoginResponse;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.UtenteResponsePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.AuthService;
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
        Utente u = authService.salvaUtente(utToSave);
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


}

package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Ditta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewPasswordPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/utenti")
public class UserController {
    @Autowired
    private AuthService authService;

    //get utenti
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Utente> filtraUtenti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome,
            @AuthenticationPrincipal Utente loggato
    ) {
        Ditta d = loggato.getDitta();
        return authService.findAll(page, size, sortBy, nome, cognome, d.getId());
    }

    //set to admin
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Utente setToAdmin(@PathVariable long id) {
        return authService.setToAdmin(id);
    }

    @GetMapping("/me")
    public Utente getMe(@AuthenticationPrincipal Utente loggato) {
        return authService.findById(loggato.getId());
    }

    //cambio password
    @PatchMapping("/me")
    public Utente cambiaPassword(@AuthenticationPrincipal Utente loggato,
                                 @RequestBody @Validated NewPasswordPayload body, BindingResult br) {
        if (br.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < br.getFieldErrors().size(); i++) {
                errList.add(br.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        return authService.cambiaPassword(body, loggato.getId());

    }

    //cancella utente
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaUtente(@AuthenticationPrincipal Utente loggato) {
        authService.cancellaUtente(loggato.getId());
    }


}

package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) String cognome
    ) {
        return authService.findAll(page, size, sortBy, nome, cognome);
    }

    //set to admin
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Utente setToAdmin(@PathVariable long id) {
        return authService.setToAdmin(id);
    }

}

package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/richieste")
public class RichiestaController {
    @Autowired
    private RichiestaService richiestaService;

    //dettagli richiesta
    @GetMapping("/{id}")
    public Richiesta getRichiesta(@PathVariable long id) {
        return richiestaService.findById(id);
    }

    //cancellazione richiesta
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void cancellaRichiesta(@PathVariable long id) {
        richiestaService.cancellaRichiesta(id);
    }
}

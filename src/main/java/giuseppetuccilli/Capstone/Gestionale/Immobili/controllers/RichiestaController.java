package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ImmobileService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/richieste")
public class RichiestaController {
    @Autowired
    private RichiestaService richiestaService;
    @Autowired
    private ImmobileService immobileService;

    //dettagli richiesta
    @GetMapping("/{id}")
    public Richiesta getRichiesta(@PathVariable long id) {
        return richiestaService.findById(id);
    }

    //cancellazione richiesta
    @DeleteMapping("/{id}")
   
    public void cancellaRichiesta(@PathVariable long id) {
        richiestaService.cancellaRichiesta(id);
    }

    //immobili compatibili richiesta
    @GetMapping("/{id}/incroci")
    public List<Immobile> getImmoCompatibili(@PathVariable long id) {
        return immobileService.findImmoCompatibili(id);
    }
}

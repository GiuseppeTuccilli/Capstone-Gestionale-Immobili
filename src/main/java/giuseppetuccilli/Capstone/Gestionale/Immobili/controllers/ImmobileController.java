package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ImmobileService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/immobili")
public class ImmobileController {
    @Autowired
    private ImmobileService immobileService;
    @Autowired
    private RichiestaService richiestaService;

    //dettagli immobile
    @GetMapping("/{id}")
    public Immobile getIDettImmobile(@PathVariable long id) {
        return immobileService.findById(id);
    }

    //creazione immobile
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Immobile nuovoImmobile(@RequestBody @Validated NewImmoPayload body, BindingResult valRes) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        Immobile i = immobileService.salvaImmobile(body);
        return i;
    }

    //cancellazione immobile
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void cancellaImmobile(@PathVariable long id) {
        immobileService.cancellaImmobile(id);
    }

    //richieste compatibili immobile
    @GetMapping("/{id}/incroci")
    public List<Richiesta> getRichCompatibili(@PathVariable long id) {
        return richiestaService.findRicCompatibili(id);
    }


}

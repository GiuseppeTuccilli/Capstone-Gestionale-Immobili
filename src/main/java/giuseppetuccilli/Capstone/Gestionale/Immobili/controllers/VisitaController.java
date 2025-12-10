package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Visita;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewVisitaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.VisitaService;
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
@RequestMapping("/visite")
public class VisitaController {
    @Autowired
    private VisitaService visitaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Visita nuovaVisita(@RequestBody @Validated NewVisitaPayload body, BindingResult valRes, @AuthenticationPrincipal Utente utLoggato) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        return visitaService.salvaVisita(body, utLoggato.getId());
    }

    @GetMapping("/mieVisite")
    public List<Visita> getVisiteUtente(@AuthenticationPrincipal Utente utLoggato) {
        List<Visita> visite = visitaService.findByUtente(utLoggato.getId());
        return visite;
       
    }

    @DeleteMapping("/mieVisite/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaPropriaVisita(@PathVariable long id, @AuthenticationPrincipal Utente utLoggato) {
        List<Visita> visite = visitaService.findByUtente(utLoggato.getId());
        Visita v = visitaService.findById(id);
        if (!visite.contains(v)) {
            throw new BadRequestException("non puoi eliminare le visite degli altri utenti");
        }
        visitaService.cancellaVisita(v.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Visita> getVisite(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return visitaService.findAll(page, size, sortBy);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void cancellaVisita(@PathVariable long id) {
        visitaService.cancellaVisita(id);
    }
}

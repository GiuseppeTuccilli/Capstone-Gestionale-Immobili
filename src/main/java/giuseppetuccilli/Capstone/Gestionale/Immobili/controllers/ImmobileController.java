package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.*;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.FotoForListDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.FotoImmobileService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ImmobileService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.RichiestaService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/immobili")
public class ImmobileController {
    @Autowired
    private ImmobileService immobileService;
    @Autowired
    private RichiestaService richiestaService;
    @Autowired
    private FotoImmobileService fotoImmobileService;
    @Autowired
    private VisitaService visitaService;

    //dettagli immobile
    @GetMapping("/{id}")
    public Immobile getIDettImmobile(@PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        Ditta d = loggato.getDitta();
        return immobileService.findById(id, d.getId());
    }

    //filtra immobili
    @GetMapping
    public Page<Immobile> filtraImmobili(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String comune,
            @RequestParam(required = false) String indirizzo,
            @RequestParam(required = false) String tipo,
            @AuthenticationPrincipal Utente loggato
    ) {
        Ditta d = loggato.getDitta();
        return immobileService.findAll(page, size, sortBy, provincia, comune, indirizzo, tipo, d.getId());
    }

    //creazione immobile
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Immobile nuovoImmobile(@RequestBody @Validated NewImmoPayload body, BindingResult valRes, @AuthenticationPrincipal Utente loggato
    ) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }

        Immobile i = immobileService.salvaImmobile(body, loggato.getDitta());
        return i;
    }

    //cancellazione immobile
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void cancellaImmobile(@PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        Ditta d = loggato.getDitta();
        immobileService.cancellaImmobile(id, d.getId());
    }

    //richieste compatibili immobile
    @GetMapping("/{id}/incroci")
    public List<Richiesta> getRichCompatibili(@PathVariable long id) {
        return richiestaService.findRicCompatibili(id);
    }

    //aggiunta foto immobile
    @PostMapping("/{id}/foto")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Immobile caricaFoto(@PathVariable long id, @RequestParam("foto") MultipartFile file, @AuthenticationPrincipal Utente loggato) {
        Ditta d = loggato.getDitta();
        System.out.println(file.getOriginalFilename());
        return immobileService.aggiungiFoto(id, file, d.getId());
    }

    //cancella foto
    @DeleteMapping("/{id}/foto/{idFoto}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaFoto(@PathVariable long id, @PathVariable long idFoto, @AuthenticationPrincipal Utente loggato) {
        Ditta d = loggato.getDitta();
        immobileService.cancellaFoto(id, idFoto, d.getId());
    }

    //lista foto immobile
    @GetMapping("/{id}/foto")
    public List<FotoForListDTO> getFotoImmobile(@PathVariable long id) {
        List<FotoImmobile> fotos = fotoImmobileService.getFotoImmobie(id);
        List<FotoForListDTO> res = new ArrayList<>();
        if (!fotos.isEmpty()) {
            for (int i = 0; i < fotos.size(); i++) {
                FotoImmobile f = fotos.get(i);
                FotoForListDTO resItem = new FotoForListDTO(f.getUrlFoto(), f.getId(), f.getImmobile().getId());
                res.add(resItem);
            }
        }
        return res;


    }

    //visite immobile
    @GetMapping("/{id}/visite")
    public List<Visita> getVisiteImmobile(
            @PathVariable long id,
            @AuthenticationPrincipal Utente loggato

    ) {
        return visitaService.findByImmobile(id, loggato);
    }


}

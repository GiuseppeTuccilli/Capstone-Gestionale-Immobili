package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.FotoImmobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.FotoForListDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.FotoImmobileService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ImmobileService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //aggiunta foto immobile
    @PostMapping("/{id}/foto")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Immobile caricaFoto(@PathVariable long id, @RequestParam("foto") MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        return immobileService.aggiungiFoto(id, file);
    }

    //cancella foto
    @DeleteMapping("/{id}/foto/{idFoto}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaFoto(@PathVariable long id, @PathVariable long idFoto) {
        immobileService.cancellaFoto(id, idFoto);
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


}

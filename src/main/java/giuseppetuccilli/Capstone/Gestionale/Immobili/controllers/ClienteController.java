package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.*;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewClientePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewRichiestaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.ClienteResDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.FatturaResDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.RichiestaResDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ClienteService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.FatturaService;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private RichiestaService richiestaService;
    @Autowired
    private FatturaService fatturaService;
    @Autowired
    private VisitaService visitaService;

    //crea cliente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResDTO creaCliente(@RequestBody @Validated NewClientePayload body, BindingResult valRes, @AuthenticationPrincipal Utente loggato) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        Cliente c = clienteService.salvaCliente(body, loggato);
        return new ClienteResDTO(c.getId(), c.getNome(), c.getCognome());


    }

    //filtra clienti
    @GetMapping
    public Page<Cliente> filtraClienti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome,
            @AuthenticationPrincipal Utente loggato
    ) {
        Ditta d = loggato.getDitta();
        return clienteService.findAll(page, size, sortBy, nome, cognome, d.getId());
    }

    //dettagli cliente
    @GetMapping("/{id}")
    public Cliente getCliente(@PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        Ditta d = loggato.getDitta();
        return clienteService.findById(id, d.getId());
    }

    //modifica cliente
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ClienteResDTO modificaCliente(@RequestBody @Validated NewClientePayload body, BindingResult valRes, @PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        Ditta d = loggato.getDitta();
        Cliente c = clienteService.modificaCliente(body, id, d.getId());
        return new ClienteResDTO(c.getId(), c.getNome(), c.getCognome());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaCliente(@PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        Ditta d = loggato.getDitta();
        clienteService.cancellaCliente(id, d.getId());
    }

    //nuova richiesta
    @PostMapping("/{id}/richieste")
    @ResponseStatus(HttpStatus.CREATED)
    public RichiestaResDTO nuovaRichiesta(@RequestBody @Validated NewRichiestaPayload body, BindingResult valRes, @PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        Richiesta r = richiestaService.salvaRichiesta(body, id, loggato);
        return new RichiestaResDTO(r.getId(), r.getPrezzoMassimo(), r.getSuperficieMinimo(),
                r.getSuperficieMassimo(), r.getVaniMinimo(), r.getVaniMassimo(), r.getLocaliMinimo(),
                r.getLocaliMassimo(), r.getComune(), r.getProvincia(), r.isCantina(), r.isAscensore(),
                r.isPostoAuto(), r.isGiardinoPrivato(), r.isTerrazzo(), r.isArredato(), r.getCliente().getId(), r.getData());

    }

    //get richieste per cliente
    @GetMapping("/{id}/richieste")
    public List<RichiestaResDTO> getRichieste(@PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        List<RichiestaResDTO> res = new ArrayList<>();
        List<Richiesta> ricList = richiestaService.findByCliente(id, loggato);
        for (int i = 0; i < ricList.size(); i++) {
            Richiesta r = ricList.get(i);
            RichiestaResDTO resItem = new RichiestaResDTO(r.getId(), r.getPrezzoMassimo(), r.getSuperficieMinimo(),
                    r.getSuperficieMassimo(), r.getVaniMinimo(), r.getVaniMassimo(), r.getLocaliMinimo(),
                    r.getLocaliMassimo(), r.getComune(), r.getProvincia(), r.isCantina(), r.isAscensore(),
                    r.isPostoAuto(), r.isGiardinoPrivato(), r.isTerrazzo(), r.isArredato(), r.getCliente().getId(), r.getData());
            res.add(resItem);
        }
        return res;
    }

    //get fatture cliente
    @GetMapping("/{id}/fatture")
    public List<FatturaResDTO> getFatture(@PathVariable long id, @AuthenticationPrincipal Utente loggato) {
        List<FatturaResDTO> res = new ArrayList<>();
        List<Fattura> fatList = fatturaService.findByCliente(id, loggato);
        if (!fatList.isEmpty()) {
            for (int i = 0; i < fatList.size(); i++) {
                Fattura f = fatList.get(i);
                FatturaResDTO resItem = new FatturaResDTO(f.getNumero(), f.getCausale(), f.getImporto(), f.getData(), f.getCliente().getId());
                res.add(resItem);
            }
        }
        return res;
    }

    //get visite cliente
    @GetMapping("/{id}/visite")

    public List<Visita> getVisiteCliente(
            @PathVariable long id,
            @AuthenticationPrincipal Utente loggato

    ) {
        return visitaService.findByCliente(id, loggato);
    }


}

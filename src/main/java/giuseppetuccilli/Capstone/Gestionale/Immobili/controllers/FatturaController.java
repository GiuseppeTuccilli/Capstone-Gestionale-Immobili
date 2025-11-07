package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Fattura;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewFatturaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.FatturaResDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.FatturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fatture")
public class FatturaController {
    @Autowired
    private FatturaService fatturaService;

    //nuova fattura
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FatturaResDTO nuovaFattura(@RequestBody @Validated NewFatturaPayload body, BindingResult valRes) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        Fattura f = fatturaService.salvaFattura(body);
        return new FatturaResDTO(f.getNumero(), f.getCausale(), f.getImporto(), f.getData(), f.getCliente().getId());
    }

    @GetMapping("/{numero}")
    public Fattura getFattura(@PathVariable long numero) {
        return fatturaService.findById(numero);
    }

    //cancellazione fattura
    @DeleteMapping("/{numero}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void eliminaFattura(@PathVariable long numero) {
        fatturaService.cancellaFattura(numero);
    }
}

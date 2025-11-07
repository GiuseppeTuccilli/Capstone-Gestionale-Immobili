package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.ValidazioneFallitaExeption;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewClientePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.ClienteResDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResDTO creaCliente(@RequestBody @Validated NewClientePayload body, BindingResult valRes) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        Cliente c = clienteService.salvaCliente(body);
        return new ClienteResDTO(c.getId(), c.getNome(), c.getCognome());


    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ClienteResDTO modificaCliente(@RequestBody @Validated NewClientePayload body, BindingResult valRes, @PathVariable long id) {
        if (valRes.hasErrors()) {
            List<String> errList = new ArrayList<>();
            for (int i = 0; i < valRes.getFieldErrors().size(); i++) {
                errList.add(valRes.getFieldErrors().get(i).getDefaultMessage());
            }
            throw new ValidazioneFallitaExeption(errList);
        }
        Cliente c = clienteService.modificaCliente(body, id);
        return new ClienteResDTO(c.getId(), c.getNome(), c.getCognome());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaCliente(@PathVariable long id) {
        clienteService.cancellaCliente(id);
    }

}

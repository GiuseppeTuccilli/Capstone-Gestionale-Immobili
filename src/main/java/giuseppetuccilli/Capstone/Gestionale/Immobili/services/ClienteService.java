package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Fattura;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Visita;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewClientePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ClienteRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.FatturaRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.VisitaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private RichiestaService richiestaService;
    @Autowired
    private FatturaRepo fatturaRepo;
    @Autowired
    private VisitaRepo visitaRepo;

    public Cliente findById(long id) {
        Optional<Cliente> found = clienteRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    public Page<Cliente> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.clienteRepo.findAll(pageable);
    }

    public Cliente salvaCliente(NewClientePayload payload) {
        Cliente cliente = new Cliente(payload.nome(), payload.cognome(), payload.telefono(),
                payload.indirizzo(), payload.email());

        Cliente c = clienteRepo.save(cliente);
        return c;
    }

    public void cancellaCliente(long id) {
        Cliente found = this.findById(id);

        //cancellazione richieste
        List<Richiesta> richieste = richiestaService.findByCliente(found.getId());
        if (!richieste.isEmpty()) {
            for (int i = 0; i < richieste.size(); i++) {
                richiestaService.cancellaRichiesta(richieste.get(i).getId());
            }
        }

        //cancellazione fatture
        List<Fattura> fatture = fatturaRepo.findByCliente(found);
        if (!fatture.isEmpty()) {
            for (int i = 0; i < fatture.size(); i++) {
                fatturaRepo.delete(fatture.get(i));
            }
        }

        //cancellazione visite
        List<Visita> visite = visitaRepo.findByCliente(found);
        if (!visite.isEmpty()) {
            for (int i = 0; i < visite.size(); i++) {
                visitaRepo.delete(visite.get(i));
            }
        }

        clienteRepo.delete(found);
    }
}

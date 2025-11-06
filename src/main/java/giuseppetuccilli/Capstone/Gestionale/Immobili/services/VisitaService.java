package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Visita;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewVisitaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ClienteRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ImmobileRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.UtenteRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.VisitaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisitaService {
    @Autowired
    private VisitaRepo visitaRepo;
    @Autowired
    private ImmobileRepo immobileRepo;
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private UtenteRepo utenteRepo;

    private LocalDate getData(String data) {
        String dataString = "";
        if (data.length() > 10) {
            dataString = data.substring(0, 10);
        } else if (data.length() == 10) {
            dataString = data;
        } else {
            throw new BadRequestException("data non valida");
        }
        try {
            String[] dataArray = dataString.split("-");
            int anno = Integer.parseInt(dataArray[0]);
            int mese = Integer.parseInt(dataArray[1]);
            int giorno = Integer.parseInt(dataArray[2]);
            LocalDate dataloc = LocalDate.of(anno, mese, giorno);
            return dataloc;
        } catch (Exception ex) {
            throw new BadRequestException("data non valida");
        }
    }

    public Visita findById(long id) {
        Optional<Visita> found = visitaRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    public Visita salvaVisita(NewVisitaPayload payload) {
        LocalDate data = getData(payload.data());
        Immobile immobile;
        Cliente cliente;
        Utente utente;

        Optional<Immobile> imFound = immobileRepo.findById(payload.idImmobile());
        if (imFound.isPresent()) {
            immobile = imFound.get();
        } else {
            throw new NotFoundException(payload.idImmobile());
        }

        //controllo che l'immobile non abbia una visita per quella data
        List<Visita> visiteImm = visitaRepo.findByImmobile(immobile);
        if (!visiteImm.isEmpty()) {
            for (int i = 0; i < visiteImm.size(); i++) {
                if (visiteImm.get(i).getData() == data) {
                    throw new BadRequestException("questo immobile ha già una visita per la data " + data.toString());
                }
            }
        }

        Optional<Cliente> clFound = clienteRepo.findById(payload.idCliente());
        if (clFound.isPresent()) {
            cliente = clFound.get();
        } else {
            throw new NotFoundException(payload.idCliente());
        }

        Optional<Utente> utFound = utenteRepo.findById(payload.idUtente());
        if (utFound.isPresent()) {
            utente = utFound.get();
        } else {
            throw new NotFoundException(payload.idUtente());
        }

        Visita visita = new Visita(data, immobile, cliente, utente);
        Visita v = visitaRepo.save(visita);
        return v;

    }

    public void cancellaVisita(long id) {
        Visita found = this.findById(id);
        visitaRepo.delete(found);
    }
}

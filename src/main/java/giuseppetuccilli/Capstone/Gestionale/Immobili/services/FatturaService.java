package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Fattura;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewFatturaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ClienteRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.FatturaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FatturaService {
    @Autowired
    private FatturaRepo fatturaRepo;
    @Autowired
    private ClienteRepo clienteRepo;

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

    public Fattura findById(long id) {
        Optional<Fattura> found = fatturaRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    public Fattura salvaFattura(NewFatturaPayload payload) {
        Cliente c;
        LocalDate data = getData(payload.data());
        Optional<Cliente> clFound = clienteRepo.findById(payload.idCliente());
        if (clFound.isPresent()) {
            c = clFound.get();
        } else {
            throw new NotFoundException(payload.idCliente());
        }
        Fattura fattura = new Fattura(payload.importo(), c, payload.causale(), data);
        Fattura f = fatturaRepo.save(fattura);
        return f;
    }

    public void cancellaFattura(long id) {
        Fattura found = this.findById(id);
        fatturaRepo.delete(found);
    }
}

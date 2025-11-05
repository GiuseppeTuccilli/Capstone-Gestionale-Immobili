package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.MacroTipologiaImmobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ImmobileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImmobileService {
    @Autowired
    private ImmobileRepo immRepo;

    public Immobile findById(long id) {
        Optional<Immobile> found = immRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    public Page<Immobile> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.immRepo.findAll(pageable);
    }

    public Immobile salvaImmobile(NewImmoPayload payload) {
        MacroTipologiaImmobile macTipo;
        switch (payload.macroTipo().toLowerCase()) {
            case "destinazione ordinaria":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_ORDINARIA;
                break;
            case "destinazione speciale":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_SPECIALE;
                break;
            case "destinazione particolare":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_PARTICOLARE;
                break;
            case "entità urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            default:
                throw new BadRequestException("macro tipologia non valida");

        }

        Immobile im = new Immobile(macTipo, payload.superficie(), payload.locali(),
                payload.vani(), payload.descrizione(), payload.prezzo(), payload.cantina(),
                payload.ascensore(), payload.postoAuto(), payload.giardinoPrivato(), payload.terrazzo(),
                payload.arredato(), payload.indirizzo(), payload.comune(), payload.provincia());

        immRepo.save(im);
        return im;

    }

    
    //foto immobile
    public void cancellaImmobile(long id) {
        Immobile found = this.findById(id);
        immRepo.delete(found);
    }


}

package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.FotoImmobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Incrocio;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.MacroTipologiaImmobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.FotoImmobileRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ImmobileRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.IncrocioRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.RichiestaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImmobileService {
    @Autowired
    private ImmobileRepo immRepo;
    @Autowired
    private RichiestaRepo richiestaRepo;
    @Autowired
    private IncrocioRepo incrocioRepo;
    @Autowired
    private FotoImmobileRepo fotoImmobileRepo;

    public Immobile findById(long id) {
        Optional<Immobile> found = immRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    private void salvaIncroci(Immobile imFromDb) {
        List<Richiesta> richieste = richiestaRepo.findAll();
        if (!richieste.isEmpty()) {
            for (int i = 0; i < richieste.size(); i++) {
                Richiesta r = richieste.get(i);
                boolean ok = true;
                if (r.getPrezzoMassimo() != 0 && r.getPrezzoMassimo() < imFromDb.getPrezzo()) {
                    ok = false;
                }
                if (r.getSuperficieMassimo() != 0 && r.getSuperficieMassimo() < imFromDb.getSuperficie()) {
                    ok = false;
                }
                if (r.getSuperficieMinimo() != 0 && r.getSuperficieMinimo() > imFromDb.getSuperficie()) {
                    ok = false;
                }
                if (r.getVaniMinimo() != 0 && r.getVaniMinimo() > imFromDb.getVani()) {
                    ok = false;
                }
                if (r.getVaniMassimo() != 0 && r.getVaniMassimo() < imFromDb.getVani()) {
                    ok = false;
                }
                if (r.getLocaliMinimo() != 0 && r.getLocaliMinimo() > imFromDb.getLocali()) {
                    ok = false;
                }
                if (r.getLocaliMassimo() != 0 && r.getLocaliMassimo() < imFromDb.getLocali()) {
                    ok = false;
                }
                if (!r.getComune().equals("") && !(r.getComune().toLowerCase().equals(imFromDb.getComune().toLowerCase()))) {
                    ok = false;
                }
                if (!r.getProvincia().equals("") && !(r.getProvincia().toLowerCase().equals(imFromDb.getProvincia().toLowerCase()))) {
                    ok = false;
                }
                if (r.isCantina() && !imFromDb.isCantina()) {
                    ok = false;
                }
                if (r.isAscensore() && !imFromDb.isAscensore()) {
                    ok = false;
                }
                if (r.isArredato() && !imFromDb.isArredato()) {
                    ok = false;
                }
                if (r.isTerrazzo() && !imFromDb.isTerrazzo()) {
                    ok = false;
                }
                if (r.isPostoAuto() && !imFromDb.isPostoAuto()) {
                    ok = false;
                }
                if (r.isGiardinoPrivato() && !imFromDb.isGiardinoPrivato()) {
                    ok = false;
                }
                if (ok) {
                    Incrocio incrocio = new Incrocio(imFromDb, r);
                    incrocioRepo.save(incrocio);

                }

            }
        }
    }

    public Page<Immobile> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.immRepo.findAll(pageable);
    }

    public Immobile salvaImmobile(NewImmoPayload payload) {
        MacroTipologiaImmobile macTipo;
        switch (payload.macroTipo().toLowerCase()) {
            case "destinazione ordinaria":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_ORDINARIA;
                break;
            case "destinazione_ordinaria":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_ORDINARIA;
                break;
            case "destinazione speciale":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_SPECIALE;
                break;
            case "destinazione_speciale":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_SPECIALE;
                break;
            case "destinazione particolare":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_PARTICOLARE;
                break;
            case "destinazione_particolare":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_PARTICOLARE;
                break;
            case "entità urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            case "entità_urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            case "entita urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            case "entita_urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            default:
                throw new BadRequestException("macro tipologia non valida");

        }

        Immobile im = new Immobile(macTipo, payload.superficie(), payload.locali(),
                payload.vani(), payload.descrizione(), payload.prezzo(), payload.cantina(),
                payload.ascensore(), payload.postoAuto(), payload.giardinoPrivato(), payload.terrazzo(),
                payload.arredato(), payload.indirizzo(), payload.comune(), payload.provincia());

        Immobile imFromDb = immRepo.save(im);

        this.salvaIncroci(imFromDb);

        return imFromDb;

    }

    public Immobile modificaImmobile(NewImmoPayload payload, long imId) {
        Immobile found = this.findById(imId);
        MacroTipologiaImmobile macTipo;
        switch (payload.macroTipo().toLowerCase()) {
            case "destinazione ordinaria":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_ORDINARIA;
                break;
            case "destinazione_ordinaria":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_ORDINARIA;
                break;
            case "destinazione speciale":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_SPECIALE;
                break;
            case "destinazione_speciale":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_SPECIALE;
                break;
            case "destinazione particolare":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_PARTICOLARE;
                break;
            case "destinazione_particolare":
                macTipo = MacroTipologiaImmobile.DESTINAZIONE_PARTICOLARE;
                break;
            case "entità urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            case "entità_urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            case "entita urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            case "entita_urbana":
                macTipo = MacroTipologiaImmobile.ENTITÀ_URBANA;
                break;
            default:
                throw new BadRequestException("macro tipologia non valida");

        }
        //cancellazione incroci
        List<Incrocio> incrociPrec = incrocioRepo.findByImmobile(found);
        if (!incrociPrec.isEmpty()) {
            for (int i = 0; i < incrociPrec.size(); i++) {
                incrocioRepo.delete(incrociPrec.get(i));
            }
        }

        found.setSuperficie(payload.superficie());
        found.setLocali((payload.locali()));
        found.setVani(payload.vani());
        found.setDescrizione(payload.descrizione());
        found.setPrezzo(payload.prezzo());
        found.setIndirizzo(payload.indirizzo());
        found.setComune(payload.comune());
        found.setProvincia(payload.provincia());
        found.setCantina(payload.cantina());
        found.setAscensore(payload.ascensore());
        found.setPostoAuto(payload.postoAuto());
        found.setGiardinoPrivato(payload.giardinoPrivato());
        found.setTerrazzo(payload.terrazzo());
        found.setArredato(payload.arredato());

        Immobile im = immRepo.save(found);

        this.salvaIncroci(im);
        return im;

    }


    public void cancellaImmobile(long id) {
        Immobile found = this.findById(id);
        //cancellazione incroci
        List<Incrocio> incrociPrec = incrocioRepo.findByImmobile(found);
        if (!incrociPrec.isEmpty()) {
            for (int i = 0; i < incrociPrec.size(); i++) {
                incrocioRepo.delete(incrociPrec.get(i));
            }
        }
        //cancellazione foto
        List<FotoImmobile> fotoList = fotoImmobileRepo.findByImmobile(found);
        if (!fotoList.isEmpty()) {
            for (int i = 0; i < fotoList.size(); i++) {
                fotoImmobileRepo.delete(fotoList.get(i));
            }
        }

        immRepo.delete(found);
    }


}

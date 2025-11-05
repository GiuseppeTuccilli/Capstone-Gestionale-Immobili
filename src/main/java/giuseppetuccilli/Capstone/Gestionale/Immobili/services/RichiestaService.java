package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Incrocio;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewRichiestaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ClienteRepo;
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
public class RichiestaService {
    @Autowired
    private RichiestaRepo richiestaRepo;
    @Autowired
    private ImmobileRepo immobileRepo;
    @Autowired
    private IncrocioRepo incrocioRepo;
    @Autowired
    private ClienteRepo clienteRepo;

    public Richiesta findById(long id) {
        Optional<Richiesta> found = richiestaRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    public Page<Richiesta> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.richiestaRepo.findAll(pageable);
    }

    public void salvaIncroci(Richiesta r) {
        List<Immobile> immobili = immobileRepo.findAll();
        if (!immobili.isEmpty()) {
            for (int i = 0; i < immobili.size(); i++) {
                Immobile imFromDb = immobili.get(i);
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

    public Richiesta salvaRichiesta(NewRichiestaPayload payload, long idCliente) {
        Optional<Cliente> clienteFound = clienteRepo.findById(idCliente);
        Cliente c;
        if (clienteFound.isPresent()) {
            c = clienteFound.get();
        } else {
            throw new NotFoundException(idCliente);
        }
        Richiesta ric = new Richiesta(payload.prezzoMassimo(), payload.superficieMinimo(),
                payload.superficieMassimo(), payload.vaniMinimo(), payload.vaniMassimo(),
                payload.localiMinimo(), payload.localiMassimo(), payload.cantina(), payload.ascensore(),
                payload.postoAuto(), payload.giardinoPrivato(), payload.terrazzo(), payload.arredato(),
                payload.comune(), payload.provincia(), c);

        Richiesta r = richiestaRepo.save(ric);
        this.salvaIncroci(r);
        return r;
    }

    public Richiesta modificaRichiesta(NewRichiestaPayload payload, long idRic) {
        Richiesta found = this.findById(idRic);
        //cancellazione incroci
        List<Incrocio> incroci = incrocioRepo.findByRichiesta(found);
        if (!incroci.isEmpty()) {
            for (int i = 0; i < incroci.size(); i++) {
                incrocioRepo.delete(incroci.get(i));
            }
        }

        found.setPrezzoMassimo(payload.prezzoMassimo());
        found.setSuperficieMassimo(payload.superficieMassimo());
        found.setSuperficieMinimo(payload.superficieMinimo());
        found.setVaniMinimo(payload.vaniMinimo());
        found.setVaniMassimo(payload.vaniMassimo());
        found.setLocaliMinimo(payload.localiMinimo());
        found.setLocaliMassimo(payload.localiMassimo());
        found.setComune(payload.comune());
        found.setProvincia(payload.provincia());
        found.setCantina(payload.cantina());
        found.setAscensore(payload.ascensore());
        found.setPostoAuto(payload.postoAuto());
        found.setGiardinoPrivato(payload.giardinoPrivato());
        found.setTerrazzo(payload.terrazzo());
        found.setArredato(payload.arredato());

        Richiesta ric = richiestaRepo.save(found);
        this.salvaIncroci(ric);
        return ric;
    }

    public void cancellaRichiesta(long id) {
        Richiesta found = this.findById(id);
        //cancellazione incroci
        List<Incrocio> incroci = incrocioRepo.findAll();
        if (!incroci.isEmpty()) {
            for (int i = 0; i < incroci.size(); i++) {
                if (incroci.get(i).getRichiesta().getId() == found.getId()) {
                    incrocioRepo.delete(incroci.get(i));
                }
            }
        }
        richiestaRepo.delete(found);
    }
}

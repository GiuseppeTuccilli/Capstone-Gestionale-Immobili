package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.*;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewRichiestaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.*;
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
    @Autowired
    private ComuneRepo comuneRepo;
    @Autowired
    private ProvinciaRepo provinciaRepo;

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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
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

                if (!r.getComune().equals("")) {
                    List<Comune> comLi = comuneRepo.findByDenominazioneContainingIgnoreCase(r.getComune());
                    if (!comLi.isEmpty() && !comLi.contains(imFromDb.getComune())) {
                        ok = false;
                    }
                }

                if (!r.getProvincia().equals("") && r.getComune().equals("")) {
                    List<Provincia> proLi = provinciaRepo.findByNomeProvinciaContainingIgnoreCase(r.getProvincia());
                    if (!proLi.isEmpty() && !proLi.contains(imFromDb.getComune().getProvincia())) {
                        ok = false;
                    }
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
        /*
        Comune com = null;
        Provincia prov = null;
        if (!payload.comune().equals("")) {
            List<Comune> comList = comuneRepo.findByDenominazioneContainingIgnoreCase(payload.comune());
            if (!comList.isEmpty()) {
                com = comList.getFirst();
                prov = com.getProvincia();
            } else {
                throw new BadRequestException("denominazione comune non valida");
            }
        }
        if (payload.comune().equals("") && !payload.provincia().equals("")) {
            List<Provincia> provList = provinciaRepo.findByNomeProvinciaContainingIgnoreCase(payload.provincia());
            if (!provList.isEmpty()) {
                prov = provList.getFirst();
            } else {
                throw new BadRequestException("denominazione provincia non valida");
            }
        }
                 */
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
        /*
        Comune com = null;
        Provincia prov = null;
        if (!payload.comune().equals("")) {
            List<Comune> comList = comuneRepo.findByDenominazioneContainingIgnoreCase(payload.comune());

            if (!comList.isEmpty()) {
                com = comList.getFirst();
                prov = com.getProvincia();
            } else {
                throw new BadRequestException("denominazione comune non valida");
            }
        }
        if (payload.comune().equals("") && !payload.provincia().equals("")) {
            List<Provincia> provList = provinciaRepo.findByNomeProvinciaContainingIgnoreCase(payload.provincia());
            if (!provList.isEmpty()) {
                prov = provList.getFirst();
            } else {
                throw new BadRequestException("denominazione provincia non valida");
            }
        }

         */

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

    public List<Richiesta> findRicCompatibili(long idImmobile) {
        Optional<Immobile> imFound = immobileRepo.findById(idImmobile);
        if (!imFound.isPresent()) {
            throw new NotFoundException(idImmobile);
        }
        List<Richiesta> foundList = incrocioRepo.findRichiesteCompatibili(idImmobile);
        return foundList;
    }

    public List<Richiesta> findByCliente(long idCliente) {
        Cliente c;
        Optional<Cliente> foundCliente = clienteRepo.findById(idCliente);
        if (foundCliente.isPresent()) {
            c = foundCliente.get();
        } else {
            throw new NotFoundException(idCliente);
        }

        List<Richiesta> foundList = richiestaRepo.findByCliente(c);
        return foundList;
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

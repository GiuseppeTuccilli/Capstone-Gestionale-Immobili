package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.*;
import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.MacroTipologiaImmobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.*;
import giuseppetuccilli.Capstone.Gestionale.Immobili.specifications.ImmobileSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ImmobileService {
    private static final long MAX_SIZE = 10 * 1024 * 1024; //10 Mb ;
    private static final List<String> ALLOWED_TYPES = List.of("image/jpg", "image/jpeg", "image/png");
    @Autowired
    private ImmobileRepo immRepo;
    @Autowired
    private RichiestaRepo richiestaRepo;
    @Autowired
    private IncrocioRepo incrocioRepo;
    @Autowired
    private FotoImmobileRepo fotoImmobileRepo;
    @Autowired
    private VisitaRepo visitaRepo;
    @Autowired
    private ComuneRepo comuneRepo;
    @Autowired
    private ProvinciaRepo provinciaRepo;
    @Autowired
    private Cloudinary imageUploader;

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
                /*
                if (r.getComune() != null && !(r.getComune().equals(imFromDb.getComune()))) {
                    ok = false;
                }
                if (r.getProvincia() != null && r.getComune() == null && !(r.getProvincia().equals(imFromDb.getProvincia()))) {
                    ok = false;
                }

                 */
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

    public Page<Immobile> findAll(int pageNumber, int pageSize, String sortBy, String provincia, String comune, String indirizzo) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.immRepo.findAll(ImmobileSpecification.filtra(provincia, comune, indirizzo), pageable);
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

        // List<Comune> comuneList = comuneRepo.findByDenominazioneContainingIgnoreCase(payload.comune());
        List<Comune> comuneList = comuneRepo.findByDenominazioneIgnoreCase(payload.comune());
        Comune comune;
        if (!comuneList.isEmpty()) {
            comune = comuneList.getFirst();
        } else {
            throw new BadRequestException("denominazione comune non valida");
        }


        Immobile im = new Immobile(macTipo, payload.superficie(), payload.locali(),
                payload.vani(), payload.descrizione(), payload.prezzo(), payload.cantina(),
                payload.ascensore(), payload.postoAuto(), payload.giardinoPrivato(), payload.terrazzo(),
                payload.arredato(), payload.indirizzo(), comune);

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

        Comune com;


        List<Comune> comuneList = comuneRepo.findByDenominazioneIgnoreCase(payload.comune());
        if (!comuneList.isEmpty()) {
            com = comuneList.getFirst();

        } else {
            throw new BadRequestException("denominazione comune non valida");
        }

        //cancellazione incroci
        List<Incrocio> incrociPrec = incrocioRepo.findByImmobile(found);
        if (!incrociPrec.isEmpty()) {
            for (int i = 0; i < incrociPrec.size(); i++) {
                incrocioRepo.delete(incrociPrec.get(i));
            }
        }

        found.setSuperficie(payload.superficie());
        found.setLocali(payload.locali());
        found.setVani(payload.vani());
        found.setDescrizione(payload.descrizione());
        found.setPrezzo(payload.prezzo());
        found.setIndirizzo(payload.indirizzo());
        found.setComune(com);
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

    public List<Immobile> findImmoCompatibili(long idRichiesta) {
        Optional<Richiesta> ricFound = richiestaRepo.findById(idRichiesta);
        if (!ricFound.isPresent()) {
            throw new NotFoundException(idRichiesta);
        }
        List<Immobile> foundList = incrocioRepo.findImmobiliCompatibili(idRichiesta);
        return foundList;
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

        //cancellazione visite
        List<Visita> visite = visitaRepo.findByImmobile(found);
        if (!visite.isEmpty()) {
            for (int i = 0; i < visite.size(); i++) {
                visitaRepo.delete(visite.get(i));
            }
        }

        immRepo.delete(found);
    }

    public Immobile aggiungiFoto(long id, MultipartFile file) {
        Immobile found = this.findById(id);

        //controllo che il file non sia vuoto
        if (file.isEmpty()) {
            throw new BadRequestException("il file è vuoto");
        }
        //non superi i 10 MB
        if (file.getSize() > MAX_SIZE) {
            throw new BadRequestException("il file supera la dimensione massima consentita");
        }
        //sia di tipo jpg, jpeg o png
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("formato del file non supportato");
        }
        //numero massimo di foto


        try {
            Map res = imageUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imgUrl = (String) res.get("url");
            FotoImmobile foto = new FotoImmobile(imgUrl, found);
            FotoImmobile fotoToAdd = fotoImmobileRepo.save(foto);
            return found;

        } catch (IOException ex) {
            throw new BadRequestException("errore nell'upload");
        }
    }

    public void cancellaFoto(long idIm, long idFoto) {
        Immobile i = this.findById(idIm);
        List<FotoImmobile> fotoList = fotoImmobileRepo.findByImmobile(i);
        Optional<FotoImmobile> found = fotoImmobileRepo.findById(idFoto);
        FotoImmobile f;
        if (found.isPresent()) {
            f = found.get();
        } else {
            throw new BadRequestException("id foto non valido");
        }
        if (fotoList.isEmpty() || !fotoList.contains(f)) {
            throw new BadRequestException("questo immobile non ha foto associate");
        }
        fotoImmobileRepo.delete(f);
    }


}

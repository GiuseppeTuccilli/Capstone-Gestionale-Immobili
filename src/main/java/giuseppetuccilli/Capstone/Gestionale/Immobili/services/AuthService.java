package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.configs.security.JWTTools;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.CodiceResetPassword;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Ditta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Visita;
import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.RuoliUtente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.UnauthorizedException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.LoginRequest;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewPasswordPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.RegistUtentePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.CodiceResetPasswordRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.DittaRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.UtenteRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.VisitaRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.specifications.UtenteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UtenteRepo utenteRepo;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private VisitaRepo visitaRepo;
    @Autowired
    private DittaRepo dittaRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CodiceResetPasswordRepo codiceResetPasswordRepo;

    public Utente findById(long id) {
        Optional<Utente> found = utenteRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    public Page<Utente> findAll(int pageNumber, int pageSize, String sortBy, String nome, String cognome, long idDitta) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return this.utenteRepo.findAll(UtenteSpecification.filtra(nome, cognome, idDitta), pageable);
    }

    public Utente findByEmail(String email) {
        Optional<Utente> found = utenteRepo.findByEmail(email);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new BadRequestException("email " + email + " non presente nel database");
        }
    }


    public Utente salvaUtente(RegistUtentePayload payload, Utente admin) {
        Optional<Utente> foundAllready = utenteRepo.findByEmail(payload.email());
        if (foundAllready.isPresent()) {
            throw new BadRequestException("l'email " + payload.email() + " è già in uso");
        }
        Ditta d = admin.getDitta();
        Utente utente = new Utente(payload.nome(), payload.cognome(), payload.email(), payload.password(), payload.telefono(), RuoliUtente.USER, d);
        Utente u = utenteRepo.save(utente);
        return u;
    }

    public Utente salvaAdmin(RegistUtentePayload payload) {
        Optional<Utente> foundAllready = utenteRepo.findByEmail(payload.email());
        if (foundAllready.isPresent()) {
            throw new BadRequestException("l'email " + payload.email() + " è già in uso");
        }
        Ditta ditta = new Ditta();
        Ditta d = dittaRepo.save(ditta);
        Utente utente = new Utente(payload.nome(), payload.cognome(), payload.email(), payload.password(), payload.telefono(), RuoliUtente.ADMIN, d);
        Utente u = utenteRepo.save(utente);
        return u;
    }

    public String checkAndGenerate(LoginRequest body) {
        Utente found = this.findByEmail(body.getEmail());
        if (bcrypt.matches(body.getPassword(), found.getPassword())) {
            return jwtTools.creaToken(found);
        } else {
            throw new UnauthorizedException("credenziali non corrette");
        }
    }

    //admin può rendere admin uno user
    public Utente setToAdmin(long id) {
        Utente found = this.findById(id);
        if (found.getRuolo() == RuoliUtente.ADMIN) {
            throw new BadRequestException("questo utente ha già il ruolo ADMIN");
        }
        found.setRuolo(RuoliUtente.ADMIN);
        return utenteRepo.save(found);
    }

    //cambio password
    public Utente cambiaPassword(NewPasswordPayload payload, long id) {
        Utente found = this.findById(id);
        found.setPassword(bcrypt.encode(payload.password()));
        return utenteRepo.save(found);

    }

    //cancellazione utente
    public void cancellaUtente(long id) {
        Utente found = this.findById(id);
        List<Visita> visite = visitaRepo.findByUtente(found);
        if (!visite.isEmpty()) {
            for (int i = 0; i < visite.size(); i++) {
                visitaRepo.delete(visite.get(i));
            }
        }
        utenteRepo.delete(found);
    }

    //reset password dimenticata
    //1) invio e salvataggio codice casuale
    public void inviaCodice(String email) {
        Optional<Utente> found = utenteRepo.findByEmail(email);
        Utente u;
        if (found.isPresent()) {
            u = found.get();
        } else {
            throw new BadRequestException("email non presente nel database");
        }
        //cancellazione eventuali altri codici dell'utente
        List<CodiceResetPassword> listaCodice = codiceResetPasswordRepo.findByUtente(u);
        if (!listaCodice.isEmpty()) {
            for (int i = 0; i < listaCodice.size(); i++) {
                codiceResetPasswordRepo.delete(listaCodice.get(i));
            }
        }

        StringBuilder codiceSb = new StringBuilder();
        String caratteri = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int indice = 0;
        int lunghezza = 0;
        Random rdm = new Random();
        boolean ok = true;
        String codice = "";

        while (ok) {
            lunghezza = rdm.nextInt(3) + 7;

            for (int i = 0; i < lunghezza; i++) {
                indice = rdm.nextInt(caratteri.length());
                codiceSb.append(caratteri.charAt(indice));
            }
            codice = codiceSb.toString();

            //ripete la pocedura nel remoto caso in cui il codice esista già
            List<CodiceResetPassword> codiciUguali = codiceResetPasswordRepo.findByCodice(codice);
            if (codiciUguali.isEmpty()) {
                ok = false;
            }
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scadenza = now.plusHours(1);

        CodiceResetPassword codResetPass = new CodiceResetPassword(codice, scadenza, u);
        codiceResetPasswordRepo.save(codResetPass);

        String mes = "il tuo codice per il reset della password è: " + codice;

        emailService.inviaEmail(u, "Codice per il reset della password", mes);

    }

    //2) verifica codie e cambio password
    public void verificaAndResetta(String codice, String newPassword) {
        List<CodiceResetPassword> codici = codiceResetPasswordRepo.findByCodice(codice);
        CodiceResetPassword codResPass;
        if (codici.isEmpty()) {
            throw new BadRequestException("codice errato");
        } else {
            codResPass = codici.getFirst();
        }

        LocalDateTime now = LocalDateTime.now();
        if (codResPass.getScadenza().isBefore(now)) {
            throw new BadRequestException("il codice è scaduto");
        }

        Utente utente = codResPass.getUtente();
        utente.setPassword(bcrypt.encode(newPassword));
        utenteRepo.save(utente);
        codiceResetPasswordRepo.delete(codResPass);
    }


}

package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.configs.security.JWTTools;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.RuoliUtente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.BadRequestException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.UnauthorizedException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.LoginRequest;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.RegistUtentePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.UtenteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UtenteRepo utenteRepo;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private JWTTools jwtTools;

    public Utente findById(long id) {
        Optional<Utente> found = utenteRepo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException(id);
        }
    }

    public Utente findByEmail(String email) {
        Optional<Utente> found = utenteRepo.findByEmail(email);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new BadRequestException("email " + email + " non presente nel database");
        }
    }

    public Utente salvaUtente(RegistUtentePayload payload) {
        Optional<Utente> foundAllready = utenteRepo.findByEmail(payload.email());
        if (foundAllready.isPresent()) {
            throw new BadRequestException("l'email " + payload.email() + " è già in uso");
        }
        Utente utente = new Utente(payload.nome(), payload.cognome(), payload.email(), payload.password(), payload.telefono(), RuoliUtente.USER);
        Utente u = utenteRepo.save(utente);
        return u;
    }

    public Utente salvaAdmin(RegistUtentePayload payload) {
        Optional<Utente> foundAllready = utenteRepo.findByEmail(payload.email());
        if (foundAllready.isPresent()) {
            throw new BadRequestException("l'email " + payload.email() + " è già in uso");
        }
        Utente utente = new Utente(payload.nome(), payload.cognome(), payload.email(), payload.password(), payload.telefono(), RuoliUtente.ADMIN);
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
}

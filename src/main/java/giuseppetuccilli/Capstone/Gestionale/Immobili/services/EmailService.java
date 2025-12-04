package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.UtenteRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    private MailgunSender mailgunSender;
    @Autowired
    private UtenteRepo utenteRepo;


    public void inviaEmail(String og, String mes) {
        Optional<Utente> u = utenteRepo.findById(1L);
        Utente utente;
        if (u.isPresent()) {
            utente = u.get();
            mailgunSender.sendRegistrationEmail(utente, og, mes);
        } else {
            System.out.println("errore");
        }
    }

    public void addToMailgun(String email) {
        mailgunSender.addToMailgun(email);
    }


}

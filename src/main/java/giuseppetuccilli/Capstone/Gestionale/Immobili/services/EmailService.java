package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.UtenteRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private MailgunSender mailgunSender;
    @Autowired
    private UtenteRepo utenteRepo;


    public void inviaEmail(Utente utente, String og, String mes) {

        mailgunSender.sendRegistrationEmail(utente, og, mes);

    }

    public void addToMailgun(String email) {
        mailgunSender.addToMailgun(email);
    }


}

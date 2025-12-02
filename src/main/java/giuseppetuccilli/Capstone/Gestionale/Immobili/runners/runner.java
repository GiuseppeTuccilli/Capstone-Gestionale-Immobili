package giuseppetuccilli.Capstone.Gestionale.Immobili.runners;

import giuseppetuccilli.Capstone.Gestionale.Immobili.importazione.CsvImportService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewClientePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewRichiestaPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.RegistUtentePayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.*;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.AuthService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ClienteService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ImmobileService;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class runner implements CommandLineRunner {
    @Autowired
    ImmobileService imServ;
    @Autowired
    ImmobileRepo immobileRepo;
    @Autowired
    ClienteRepo clienteRepo;
    @Autowired
    ClienteService clienteService;
    @Autowired
    private RichiestaRepo richiestaRepo;
    @Autowired
    private RichiestaService richiestaService;
    @Autowired
    private ProvinciaRepo provRepo;
    @Autowired
    private ComuneRepo comRepo;
    @Autowired
    private CsvImportService importService;
    @Autowired
    private PasswordEncoder bCrypt;
    @Autowired
    private UtenteRepo utenteRepo;
    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {

        Path provincePath = Paths.get("src/province-italiane.csv");
        Path comunePath = Paths.get("src/comuni-italiani.csv");

        if (provRepo.findAll().isEmpty()) {
            importService.salvaProvince(provincePath);
            System.out.println("tutto bene province");
        }
        if (comRepo.findAll().isEmpty()) {
            importService.salvaComuni(comunePath);
            System.out.println("tutto bene comuni");
        }


        NewImmoPayload imPay = new NewImmoPayload("entità urbana", 100, 5, 5, "descrizione immobile di prova 1", 100000, "indirizzo di prova n 1", "Esperia", false, true, false, false, false, true);
        NewImmoPayload imPay2 = new NewImmoPayload("entità urbana", 150, 10, 6, "descrizione immobile di prova 2", 150000, "indirizzo di prova n 2", "Pontecorvo", true, true, true, false, false, true);
        NewImmoPayload imPay3 = new NewImmoPayload("entità urbana", 200, 12, 7, "descrizione immobile di prova 3", 200000, "indirizzo di prova n 3", "Cassino", true, true, true, true, true, true);
        NewImmoPayload imPay4 = new NewImmoPayload("destinazione speciale", 200, 12, 7, "descrizione immobile di prova 4", 180000, "indirizzo di prova n 4", "Formia", true, true, true, true, true, true);
        NewImmoPayload imPay5 = new NewImmoPayload("destinazione particolare", 200, 12, 7, "descrizione immobile di prova 5", 190000, "indirizzo di prova n 5", "Latina", true, true, true, true, true, true);
        List<NewImmoPayload> listImmobili = new ArrayList<NewImmoPayload>();
        for (int i = 0; i < 6; i++) {
            listImmobili.add(new NewImmoPayload("entità urbana", (100 + (i * 10)), (5 + i), (5 + i), "descrizione immobile di prova " + (6 + i), (100000 + (i * 10000)), "indirizzo di prova n " + (6 + i), "Formia", true, false, true, true, true, true));
        }
        for (int i = 0; i < 6; i++) {
            listImmobili.add(new NewImmoPayload("destinazione speciale", (100 + (i * 10)), (5 + i), (5 + i), "descrizione immobile di prova " + (12 + i), (100000 + (i * 10000)), "indirizzo di prova n " + (12 + i), "Cassino", true, false, true, true, true, true));
        }
        for (int i = 0; i < 6; i++) {
            listImmobili.add(new NewImmoPayload("destinazione particolare", (100 + (i * 10)), (5 + i), (5 + i), "descrizione immobile di prova " + (17 + i), (100000 + (i * 10000)), "indirizzo di prova n " + (17 + i), "Latina", true, false, true, true, true, true));
        }

        if (immobileRepo.findAll().isEmpty()) {
            imServ.salvaImmobile(imPay);
            imServ.salvaImmobile(imPay2);
            imServ.salvaImmobile(imPay3);
            imServ.salvaImmobile(imPay4);
            imServ.salvaImmobile(imPay5);
            for (int i = 0; i < listImmobili.size(); i++) {
                NewImmoPayload im = listImmobili.get(i);
                imServ.salvaImmobile(im);
            }
        }

        NewClientePayload cl = new NewClientePayload("Aldo", "Baglio", "012547846", "indirizzo cilente n 1", "aldo@baglio.com");
        NewClientePayload cl2 = new NewClientePayload("Giovanni", "Storti", "012547846", "indirizzo cilente n 2", "giovanni@storti.com");
        if (clienteRepo.findAll().isEmpty()) {
            clienteService.salvaCliente(cl);
            clienteService.salvaCliente(cl2);
        }

        NewRichiestaPayload r = new NewRichiestaPayload(150000, 50, 0, 5, 0, 6, 0, "Pontecorvo", "", false, false, false, false, false, false);
        NewRichiestaPayload r2 = new NewRichiestaPayload(2000000, 50, 0, 5, 0, 5, 0, "", "", false, false, false, false, false, false);


        RegistUtentePayload ut = new RegistUtentePayload("Mario", "Rossi", "marioRossi@gmail.com", bCrypt.encode("12345678"), "2541526987");
        RegistUtentePayload ut2 = new RegistUtentePayload("Dario", "Bianchi", "darioBianchi@gmail.com", bCrypt.encode("12345678"), "2541526987");

        if (utenteRepo.findAll().isEmpty()) {
            authService.salvaAdmin(ut);
            authService.salvaUtente(ut2);
        }


    }
}

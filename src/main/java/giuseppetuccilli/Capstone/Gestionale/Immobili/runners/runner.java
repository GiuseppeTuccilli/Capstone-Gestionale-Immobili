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


        NewImmoPayload imPay = new NewImmoPayload("entità urbana", 100, 5, 5, "bello", 100000, "via vattela pesca", "Esperia", false, true, false, false, false, true);
        NewImmoPayload imPay2 = new NewImmoPayload("entità urbana", 150, 10, 6, "molto bello", 150000, "via vattela pesca 2", "Pontecorvo", true, true, true, false, false, true);
        NewImmoPayload imPay3 = new NewImmoPayload("entità urbana", 200, 12, 7, "bellissimo", 200000, "via vattela pesca 3", "Cassino", true, true, true, true, true, true);
        if (immobileRepo.findAll().isEmpty()) {
            imServ.salvaImmobile(imPay);
            imServ.salvaImmobile(imPay2);
            imServ.salvaImmobile(imPay3);
        }

        NewClientePayload cl = new NewClientePayload("pippo", "rossi", "012547846", "via non saprei", "pippo@gmail.com");
        NewClientePayload cl2 = new NewClientePayload("pluto", "bianchi", "012547846", "via non saprei 2", "plutoo@gmail.com");
        if (clienteRepo.findAll().isEmpty()) {
            clienteService.salvaCliente(cl);
            clienteService.salvaCliente(cl2);
        }

        NewRichiestaPayload r = new NewRichiestaPayload(150000, 50, 0, 5, 0, 6, 0, "Pontecorvo", "", false, false, false, false, false, false);
        NewRichiestaPayload r2 = new NewRichiestaPayload(2000000, 50, 0, 5, 0, 5, 0, "", "", false, false, false, false, false, false);


        RegistUtentePayload ut = new RegistUtentePayload("mario", "rossi", "marioRossi@gmail.com", bCrypt.encode("12345678"), "2541526987");
        RegistUtentePayload ut2 = new RegistUtentePayload("dario", "bianchi", "darioBianchi@gmail.com", bCrypt.encode("12345678"), "2541526987");

        if (utenteRepo.findAll().isEmpty()) {
            authService.salvaAdmin(ut);
            authService.salvaUtente(ut2);
        }

       
    }
}

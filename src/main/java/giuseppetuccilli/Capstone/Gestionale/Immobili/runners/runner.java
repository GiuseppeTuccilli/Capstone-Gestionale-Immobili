package giuseppetuccilli.Capstone.Gestionale.Immobili.runners;

import giuseppetuccilli.Capstone.Gestionale.Immobili.importazione.CsvImportService;
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
    @Autowired
    private DittaRepo dittaRepo;

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


    }
}

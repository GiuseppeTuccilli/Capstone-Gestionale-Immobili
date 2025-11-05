package giuseppetuccilli.Capstone.Gestionale.Immobili.runners;

import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests.NewImmoPayload;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ImmobileRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ImmobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class runner implements CommandLineRunner {
    @Autowired
    ImmobileService imServ;
    @Autowired
    ImmobileRepo immobileRepo;

    @Override
    public void run(String... args) throws Exception {
        NewImmoPayload imPay = new NewImmoPayload("entità urbana", 100, 5, 5, "bello", 100000, "via vattela pesca", "Esperia", "FR", false, true, false, false, false, true);
        if (immobileRepo.findAll().isEmpty()) {
            imServ.salvaImmobile(imPay);
        }


    }
}

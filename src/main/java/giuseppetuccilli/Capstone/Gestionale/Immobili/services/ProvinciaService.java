package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Provincia;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ProvinciaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinciaService {
    @Autowired
    private ProvinciaRepo provinciaRepo;

    public List<Provincia> findAll() {
        return provinciaRepo.findAll();
    }
}

package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Comune;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Provincia;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ComuneRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ProvinciaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComuneService {
    @Autowired
    private ComuneRepo comuneRepo;
    @Autowired
    private ProvinciaRepo provinciaRepo;

    public List<Comune> findByProvincia(long idProv) {
        Optional<Provincia> found = provinciaRepo.findById(idProv);
        Provincia p;
        if (found.isPresent()) {
            p = found.get();
        } else {
            throw new NotFoundException(idProv);
        }
        return comuneRepo.findByProvincia(p);

    }
}

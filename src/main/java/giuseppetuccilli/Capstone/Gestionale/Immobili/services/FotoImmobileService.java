package giuseppetuccilli.Capstone.Gestionale.Immobili.services;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.FotoImmobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions.NotFoundException;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.FotoImmobileRepo;
import giuseppetuccilli.Capstone.Gestionale.Immobili.repositories.ImmobileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FotoImmobileService {
    @Autowired
    private FotoImmobileRepo fotoImmobileRepo;
    @Autowired
    private ImmobileRepo immobileRepo;

    public List<FotoImmobile> getFotoImmobie(long idImmobile) {
        Optional<Immobile> found = immobileRepo.findById(idImmobile);
        Immobile i;
        if (found.isPresent()) {
            i = found.get();
        } else {
            throw new NotFoundException(idImmobile);
        }
        return fotoImmobileRepo.findByImmobile(i);
    }
}

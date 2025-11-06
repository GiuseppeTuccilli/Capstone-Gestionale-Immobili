package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Comune;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComuneRepo extends JpaRepository<Comune, Long> {
    List<Comune> findByDenominazioneContainingIgnoreCase(String denominazione);

    List<Comune> findByProvincia(Provincia provincia);
}

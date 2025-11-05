package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.FotoImmobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoImmobileRepo extends JpaRepository<FotoImmobile, Long> {
    List<FotoImmobile> findByImmobile(Immobile immobile);
}

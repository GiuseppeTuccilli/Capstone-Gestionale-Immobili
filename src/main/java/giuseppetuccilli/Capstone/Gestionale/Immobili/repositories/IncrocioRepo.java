package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Incrocio;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncrocioRepo extends JpaRepository<Incrocio, Long> {
    List<Incrocio> findByImmobile(Immobile immobile);

    List<Incrocio> findByRichiesta(Richiesta richiesta);
}

package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Incrocio;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncrocioRepo extends JpaRepository<Incrocio, Long> {
    List<Incrocio> findByImmobile(Immobile immobile);

    List<Incrocio> findByRichiesta(Richiesta richiesta);

    @Query("SELECT i FROM Incrocio c JOIN c.Immobile i WHERE c.Richiesta.id = :richiestaId")
    List<Immobile> findImmobiliCompatibili(@Param("richiestaId") Long richiestaId);

    @Query("SELECT r FROM Incrocio c JOIN c.Richiesta r WHERE c.Immobile.id = :immobileId")
    List<Richiesta> findRichiesteCompatibili(@Param("immobileId") Long immobileId);
}

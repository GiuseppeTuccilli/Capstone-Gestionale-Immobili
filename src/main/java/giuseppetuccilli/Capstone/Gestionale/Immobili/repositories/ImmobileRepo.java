package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Ditta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmobileRepo extends JpaRepository<Immobile, Long>, JpaSpecificationExecutor<Immobile> {
    List<Immobile> findByDitta(Ditta ditta);
}

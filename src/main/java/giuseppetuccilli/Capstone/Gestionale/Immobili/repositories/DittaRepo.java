package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Ditta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DittaRepo extends JpaRepository<Ditta, Long> {
}

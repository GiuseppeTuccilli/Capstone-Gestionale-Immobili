package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Ditta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepo extends JpaRepository<Utente, Long>, JpaSpecificationExecutor<Utente> {
    Optional<Utente> findByEmail(String email);

    List<Utente> findByDitta(Ditta ditta);
}

package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitaRepo extends JpaRepository<Visita, Long> {
    List<Visita> findByCliente(Cliente cliente);

    List<Visita> findByImmobile(Immobile immobile);

    List<Visita> findByUtente(Utente utente);
}

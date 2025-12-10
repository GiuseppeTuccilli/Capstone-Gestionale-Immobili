package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Ditta;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RichiestaRepo extends JpaRepository<Richiesta, Long> {
    List<Richiesta> findByCliente(Cliente cliente);

    @Query("SELECT r FROM Richiesta r WHERE r.cliente.ditta = :ditta")
    List<Richiesta> findByDitta(@Param("ditta") Ditta ditta);
}

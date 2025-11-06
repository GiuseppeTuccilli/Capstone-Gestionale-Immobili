package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepo extends JpaRepository<Provincia, Long> {
    List<Provincia> findByNomeProvinciaContainingIgnoreCase(String nomeProvincia);

}

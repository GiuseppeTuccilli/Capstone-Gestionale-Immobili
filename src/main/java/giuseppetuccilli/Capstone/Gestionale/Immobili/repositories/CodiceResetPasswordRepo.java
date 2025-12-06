package giuseppetuccilli.Capstone.Gestionale.Immobili.repositories;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.CodiceResetPassword;
import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodiceResetPasswordRepo extends JpaRepository<CodiceResetPassword, Long> {

    List<CodiceResetPassword> findByUtente(Utente utente);

    List<CodiceResetPassword> findByCodice(String codice);
}

package giuseppetuccilli.Capstone.Gestionale.Immobili.specifications;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Cliente;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecification {
    public static Specification<Cliente> filtra(
            String nome,
            String cognome,
            long idDitta
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            predicate = cb.and(predicate, cb.equal(root.get("ditta").get("id"), idDitta));

            if (nome != null && !nome.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }

            if (cognome != null && !cognome.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("cognome")), "%" + cognome.toLowerCase() + "%"));
            }
            return predicate;
        };
    }
}

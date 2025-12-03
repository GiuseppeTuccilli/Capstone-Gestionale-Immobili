package giuseppetuccilli.Capstone.Gestionale.Immobili.specifications;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Immobile;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ImmobileSpecification {
    public static Specification<Immobile> filtra(
            String provincia,
            String comune,
            String indirizzo,
            String tipo,
            long idDitta

    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            predicate = cb.and(predicate, cb.equal(root.get("ditta").get("id"), idDitta));


            if (indirizzo != null && !indirizzo.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("indirizzo")), "%" + indirizzo.toLowerCase() + "%"));
            }

            if (provincia != null && !provincia.isEmpty() && (comune == null || comune.isEmpty())) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("comune").get("provincia").get("nomeProvincia")),
                        "%" + provincia.toLowerCase() + "%"));
            }

            if (comune != null && !comune.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("comune").get("denominazione")),
                        "%" + comune.toLowerCase() + "%"));
            }

            if (tipo != null && !tipo.isEmpty()) {
                predicate = cb.and(predicate, cb.like(root.get("MacroTipologia"), tipo));
            }
            return predicate;

        };
    }
}

package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses;

import java.time.LocalDate;

public record VisitaResDTO(
        long idVisita,
        long idUtente,
        long idCliente,
        LocalDate data
) {
}

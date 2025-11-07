package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses;

import java.time.LocalDate;

public record VisitaResAdminDTO(
        long idVisita,
        long idCliente,
        long idImmobile,
        LocalDate data,
        long idUtente
) {
}

package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses;

import java.time.LocalDate;

public record FatturaResDTO(
        long numero,
        String causale,
        double importo,
        LocalDate data,
        long idCliente

) {
}

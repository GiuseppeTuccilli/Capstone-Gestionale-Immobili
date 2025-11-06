package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewVisitaPayload(
        @NotBlank
        @Size(min = 10)
        String data,
        @NotNull
        long idImmobile,
        @NotNull
        long idCliente,
        @NotNull
        long idUtente
) {
}

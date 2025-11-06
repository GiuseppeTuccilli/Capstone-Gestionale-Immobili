package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NewFatturaPayload(
        @NotNull
        @Positive
        double importo,
        @NotBlank
        @Size(min = 10)
        String data,
        String causale,
        @NotNull
        @Positive
        long idCliente
) {
}

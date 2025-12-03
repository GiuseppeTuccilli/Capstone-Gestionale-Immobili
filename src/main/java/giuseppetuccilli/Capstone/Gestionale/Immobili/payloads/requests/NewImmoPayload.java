package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewImmoPayload(
        @NotBlank
        String macroTipo,
        @NotNull
        double superficie,
        @NotNull
        int locali,
        @NotNull
        int vani,
        String descrizione,
        @NotNull
        @Positive
        double prezzo,
        @NotBlank
        String indirizzo,
        @NotBlank
        String comune,

        boolean cantina,
        boolean ascensore,
        boolean postoAuto,
        boolean giardinoPrivato,
        boolean terrazzo,
        boolean arredato


) {
}

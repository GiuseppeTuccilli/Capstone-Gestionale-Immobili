package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.PositiveOrZero;

public record NewRichiestaPayload(
        @PositiveOrZero
        double prezzoMassimo,
        @PositiveOrZero
        double superficieMinimo,
        @PositiveOrZero
        double superficieMassimo,
        @PositiveOrZero
        int vaniMinimo,
        @PositiveOrZero
        int vaniMassimo,
        @PositiveOrZero
        int localiMinimo,
        @PositiveOrZero
        int localiMassimo,
        String comune,
        String provincia,
        boolean cantina,
        boolean ascensore,
        boolean postoAuto,
        boolean giardinoPrivato,
        boolean terrazzo,
        boolean arredato


) {
}

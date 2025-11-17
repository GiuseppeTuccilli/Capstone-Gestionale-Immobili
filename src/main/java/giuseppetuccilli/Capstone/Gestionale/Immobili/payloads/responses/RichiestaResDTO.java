package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses;

import java.time.LocalDate;

public record RichiestaResDTO(
        long id,
        double prezzoMassimo,
        double superficieMinimo,
        double superficieMassimo,
        int vaniMinimo,
        int vaniMassimo,
        int localiMinimo,
        int localiMassimo,
        String comune,
        String provincia,
        boolean cantina,
        boolean ascensore,
        boolean postoAuto,
        boolean giardinoPrivato,
        boolean terrazzo,
        boolean arredato,
        long idCliente,
        LocalDate data
) {
}

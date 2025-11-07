package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses;

public record FotoForListDTO(
        String urlFoto,
        long idFoto,
        long idImmobile
) {
}

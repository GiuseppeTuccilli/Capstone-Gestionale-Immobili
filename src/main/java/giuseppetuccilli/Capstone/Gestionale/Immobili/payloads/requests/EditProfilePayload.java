package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditProfilePayload(
        @NotBlank
        @Size(min = 2)
        String newNome,
        @NotBlank
        @Size(min = 2)
        String newCognome,
        String newTelefono
) {
}

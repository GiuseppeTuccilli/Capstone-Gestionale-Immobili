package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistUtentePayload(
        @NotBlank
        @Size(min = 2)
        String nome,
        @NotBlank
        @Size(min = 2)
        String cognome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 8)
        String password,
        String telefono


) {
}

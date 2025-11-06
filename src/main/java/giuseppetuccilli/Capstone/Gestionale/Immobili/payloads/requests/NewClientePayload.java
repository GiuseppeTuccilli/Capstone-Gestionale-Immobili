package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewClientePayload(
        @NotBlank
        @Size(min = 2, max = 30)
        String nome,
        @NotBlank
        @Size(min = 2, max = 30)
        String cognome,
        String telefono,
        @NotBlank
        String indirizzo,
        @NotBlank
        @Email
        String email
        
) {
}

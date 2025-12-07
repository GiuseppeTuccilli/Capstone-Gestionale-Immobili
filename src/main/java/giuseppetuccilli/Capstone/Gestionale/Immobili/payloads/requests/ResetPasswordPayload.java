package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordPayload(
        @NotBlank
        @Size(min = 7)
        String codice,
        @NotBlank
        @Size(min = 8)
        String newPassword,
        @NotBlank
        @Email
        String email
) {

}

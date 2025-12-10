package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailPayload(
        @NotBlank
        @Email
        String newEmail
) {
}

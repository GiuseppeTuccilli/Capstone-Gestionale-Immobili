package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.requests;

import jakarta.validation.constraints.Size;

public record NewPasswordPayload(
        @Size(min = 8)
        String password
) {
}

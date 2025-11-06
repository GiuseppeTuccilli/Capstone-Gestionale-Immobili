package giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses;


import java.util.List;

public record ErrorsWithListDTO(String message,

                                List<String> errorsList) {
}

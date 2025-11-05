package giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("la risorsa con id: " + id + " non è presente nel database");
    }
}

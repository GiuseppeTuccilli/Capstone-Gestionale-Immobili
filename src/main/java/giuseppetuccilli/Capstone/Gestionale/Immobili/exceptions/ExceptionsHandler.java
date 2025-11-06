package giuseppetuccilli.Capstone.Gestionale.Immobili.exceptions;


import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.ErrorsDTO;
import giuseppetuccilli.Capstone.Gestionale.Immobili.payloads.responses.ErrorsWithListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFound(NotFoundException ex) {
        return new ErrorsDTO(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleBadRequest(BadRequestException ex) {
        return new ErrorsDTO(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage());
    }

    @ExceptionHandler(ValidazioneFallitaExeption.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsWithListDTO handleValidazioneFallita(ValidazioneFallitaExeption ex) {
        return new ErrorsWithListDTO(ex.getMessage(), ex.getMsgList());

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleServiceError(Exception ex) {
        ex.printStackTrace();
        return new ErrorsDTO("c'è stato un errore generico, risolviamo il prima possibile");
    }
}

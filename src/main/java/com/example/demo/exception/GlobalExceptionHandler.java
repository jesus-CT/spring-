import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> onDataIntegrityViolation(DataIntegrityViolationException ex) {
        var body = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Ya existe un registro con ese valor único."
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body);
    }

    public static class ErrorResponse {
        public int status;
        public String message;
        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}

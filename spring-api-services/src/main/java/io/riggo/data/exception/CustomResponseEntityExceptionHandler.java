package io.riggo.data.exception;


import io.riggo.web.integration.exception.PayloadParseException;
import io.riggo.web.response.BaseAPIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

    @ExceptionHandler({Exception.class, RiggoDataAccessException.class})
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity(buildAPIResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class, PayloadParseException.class})
    public final ResponseEntity<Object> badRequestExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity(buildAPIResponse(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class, LoadNotFoundException.class})
    public final ResponseEntity<Object> notFoundExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity(buildAPIResponse(ex, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class})
    public final ResponseEntity<Object> conflictException(Exception ex, WebRequest request) {
        return new ResponseEntity(buildAPIResponse(ex, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }

    private BaseAPIResponse<?> buildAPIResponse(Exception ex, HttpStatus httpStatus) {
        logger.error(ex.getClass().getName(), ex);
        BaseAPIResponse<?> baseAPIResponse = new BaseAPIResponse();
        baseAPIResponse.setMessage(ex.getMessage());
        baseAPIResponse.setStatus(httpStatus.value());
        return baseAPIResponse;
    }
}

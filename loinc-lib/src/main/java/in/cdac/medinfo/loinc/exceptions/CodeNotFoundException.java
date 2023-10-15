package in.cdac.medinfo.loinc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * @author: Cheenmaya Bhutad
 * Exception handling for code not found 
 * 
 */

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CodeNotFoundException extends RuntimeException {

    private String message;

    public CodeNotFoundException(String message) {
        super(message);    
    }
    
}

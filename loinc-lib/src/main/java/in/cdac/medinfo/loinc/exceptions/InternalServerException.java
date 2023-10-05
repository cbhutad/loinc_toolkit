package in.cdac.medinfo.loinc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author cheenmayab
 * Exception handling for Internal Server Error
 *
 */

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {

	private static final long serialVersionId = 1L;

	public InternalServerException(String message) {
		super(message);
	}
}

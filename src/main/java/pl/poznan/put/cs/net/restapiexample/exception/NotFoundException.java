package pl.poznan.put.cs.net.restapiexample.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7847069063303913250L;

	public NotFoundException() {
		super("Could not find requested resource.");
	}
}

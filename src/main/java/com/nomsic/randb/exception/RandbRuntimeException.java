package com.nomsic.randb.exception;

public class RandbRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6561115726968717130L;

	public RandbRuntimeException() {
		super();
	}

	public RandbRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RandbRuntimeException(String message) {
		super(message);
	}

	public RandbRuntimeException(Throwable cause) {
		super(cause);
	}

	
}

package com.nomsic.randb.exception;

public class InitException extends RandbRuntimeException {

	private static final long serialVersionUID = 4195930385826072954L;

	public InitException() {
		super();
	}

	public InitException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitException(String message) {
		super(message);
	}

	public InitException(Throwable cause) {
		super(cause);
	}
	
}

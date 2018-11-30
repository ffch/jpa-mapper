package com.github.ffch.jpamapper.core.exception;


public class JpaMapperException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8004374884229964452L;

	public JpaMapperException() {
        super();
    }

    public JpaMapperException(String message) {
        super(message);
    }

    public JpaMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaMapperException(Throwable cause) {
        super(cause);
    }

}

package com.smartkaya.exception;

public class KayaException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2844375212585735059L;

	/**
     * Creates a new DBException.
     */
    public KayaException() {
        super();
    }

    /**
     * Constructs a new DBException.
     *
     * @param message the reason for the exception
     */
    public KayaException(String message) {
        super(message);
    }

    /**
     * Constructs a new DBException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public KayaException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DBException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public KayaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DBException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     * @param enableSuppression
     * @param writableStackTrace
     */
	public KayaException(String message, Throwable cause,

			boolean enableSuppression, boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);

	}
}

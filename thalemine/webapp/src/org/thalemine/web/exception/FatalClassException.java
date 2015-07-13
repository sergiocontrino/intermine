package org.thalemine.web.exception;


@SuppressWarnings("serial")
public class FatalClassException extends ClassException {

	/**
	 * Create a new FatalBeanException with the specified message.
	 * @param msg the detail message
	 */
	public FatalClassException(String msg) {
		super(msg);
	}

	/**
	 * Create a new FatalBeanException with the specified message
	 * and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public FatalClassException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

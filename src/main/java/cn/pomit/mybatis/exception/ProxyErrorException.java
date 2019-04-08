package cn.pomit.mybatis.exception;

public class ProxyErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1030141168368890994L;

	public ProxyErrorException() {
		super();
	}

	public ProxyErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProxyErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProxyErrorException(String message) {
		super(message);
	}

	public ProxyErrorException(Throwable cause) {
		super(cause);
	}
}

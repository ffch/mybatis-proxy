package cn.pomit.mybatis.exception;

public class ParamsNeedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1030141168368890994L;

	public ParamsNeedException() {
		super();
	}

	public ParamsNeedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParamsNeedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParamsNeedException(String message) {
		super(message);
	}

	public ParamsNeedException(Throwable cause) {
		super(cause);
	}
}

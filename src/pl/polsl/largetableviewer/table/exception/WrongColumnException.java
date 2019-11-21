package pl.polsl.largetableviewer.table.exception;

public class WrongColumnException extends Exception {
    public WrongColumnException() {
        super();
    }

    public WrongColumnException(String message) {
        super(message);
    }

    public WrongColumnException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongColumnException(Throwable cause) {
        super(cause);
    }

    protected WrongColumnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

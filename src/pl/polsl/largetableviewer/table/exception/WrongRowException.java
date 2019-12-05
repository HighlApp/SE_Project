package pl.polsl.largetableviewer.table.exception;

public class WrongRowException extends Exception {
    public WrongRowException() {
        super();
    }

    public WrongRowException(String message) {
        super(message);
    }

    public WrongRowException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongRowException(Throwable cause) {
        super(cause);
    }

    protected WrongRowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

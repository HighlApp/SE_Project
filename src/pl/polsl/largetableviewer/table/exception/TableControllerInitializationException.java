package pl.polsl.largetableviewer.table.exception;

public class TableControllerInitializationException extends Exception{
    public TableControllerInitializationException() {
        super();
    }

    public TableControllerInitializationException(String message) {
        super(message);
    }

    public TableControllerInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableControllerInitializationException(Throwable cause) {
        super(cause);
    }

    protected TableControllerInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

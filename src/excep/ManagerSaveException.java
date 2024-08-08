package excep;

public class ManagerSaveException extends RuntimeException { // Создаём своё исключение

    public ManagerSaveException(final Throwable cause) {
        super(cause);
    }

}
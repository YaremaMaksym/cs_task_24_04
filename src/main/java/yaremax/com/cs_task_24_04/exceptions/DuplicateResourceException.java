package yaremax.com.cs_task_24_04.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super("(DuplicateResourceException) " + message);
    }
}

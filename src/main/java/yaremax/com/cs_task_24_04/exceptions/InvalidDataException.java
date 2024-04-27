package yaremax.com.cs_task_24_04.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super("(InvalidDataException) " + message);
    }
}

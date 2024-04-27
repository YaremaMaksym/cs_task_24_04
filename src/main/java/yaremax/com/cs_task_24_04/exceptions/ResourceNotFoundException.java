package yaremax.com.cs_task_24_04.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super("(ResourceNotFoundException) " + message);
    }
}

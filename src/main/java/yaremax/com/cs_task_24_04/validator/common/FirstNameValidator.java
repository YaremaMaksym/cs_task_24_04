package yaremax.com.cs_task_24_04.validator.common;

import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.validator.Validator;

@Component
public class FirstNameValidator implements Validator<String> {
    @Override
    public void validate(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidDataException("First name cannot be null or empty");
        }
    }
}

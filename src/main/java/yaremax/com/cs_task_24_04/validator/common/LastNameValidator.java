package yaremax.com.cs_task_24_04.validator.common;

import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.validator.Validator;

@Component
public class LastNameValidator implements Validator<String> {
    @Override
    public void validate(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidDataException("Last name cannot be null or empty");
        }
    }
}

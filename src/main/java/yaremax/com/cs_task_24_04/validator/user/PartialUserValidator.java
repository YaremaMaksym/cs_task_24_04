package yaremax.com.cs_task_24_04.validator.user;

import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.User;
import yaremax.com.cs_task_24_04.validator.*;
import yaremax.com.cs_task_24_04.validator.common.BirthDateValidator;
import yaremax.com.cs_task_24_04.validator.common.EmailValidator;
import yaremax.com.cs_task_24_04.validator.common.FirstNameValidator;
import yaremax.com.cs_task_24_04.validator.common.LastNameValidator;

@Component
public class PartialUserValidator implements Validator<User> {
    private final EmailValidator emailValidator = new EmailValidator();
    private final FirstNameValidator firstNameValidator = new FirstNameValidator();
    private final LastNameValidator lastNameValidator = new LastNameValidator();
    private final BirthDateValidator birthDateValidator = new BirthDateValidator();

    @Override
    public void validate(User user) {
        if (user == null) {
            throw new InvalidDataException("User cannot be null");
        }

        if (user.getEmail() != null) {
            emailValidator.validate(user.getEmail());
        }
        if (user.getFirstName() != null) {
            firstNameValidator.validate(user.getFirstName());
        }
        if (user.getLastName() != null) {
            lastNameValidator.validate(user.getLastName());
        }
        if (user.getBirthDate() != null) {
            birthDateValidator.validate(user.getBirthDate());
        }
    }
}

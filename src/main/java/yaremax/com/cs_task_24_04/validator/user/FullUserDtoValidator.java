package yaremax.com.cs_task_24_04.validator.user;

import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.UserDto;
import yaremax.com.cs_task_24_04.validator.*;
import yaremax.com.cs_task_24_04.validator.common.BirthDateValidator;
import yaremax.com.cs_task_24_04.validator.common.EmailValidator;
import yaremax.com.cs_task_24_04.validator.common.FirstNameValidator;
import yaremax.com.cs_task_24_04.validator.common.LastNameValidator;

@Component
public class FullUserDtoValidator implements Validator<UserDto> {
    private final EmailValidator emailValidator = new EmailValidator();
    private final FirstNameValidator firstNameValidator = new FirstNameValidator();
    private final LastNameValidator lastNameValidator = new LastNameValidator();
    private final BirthDateValidator birthDateValidator = new BirthDateValidator();

    @Override
    public void validate(UserDto user) {
        if (user == null) {
            throw new InvalidDataException("User cannot be null");
        }

        emailValidator.validate(user.getEmail());
        firstNameValidator.validate(user.getFirstName());
        lastNameValidator.validate(user.getLastName());
        birthDateValidator.validate(user.getBirthDate());
    }
}

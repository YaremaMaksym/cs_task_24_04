package yaremax.com.cs_task_24_04.validator.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.UserDto;
import yaremax.com.cs_task_24_04.validator.*;
import yaremax.com.cs_task_24_04.validator.common.BirthDateValidator;
import yaremax.com.cs_task_24_04.validator.common.EmailValidator;
import yaremax.com.cs_task_24_04.validator.common.FirstNameValidator;
import yaremax.com.cs_task_24_04.validator.common.LastNameValidator;

@Component
@AllArgsConstructor
public class PartialUserDtoValidator implements Validator<UserDto> {
    private final EmailValidator emailValidator;
    private final FirstNameValidator firstNameValidator;
    private final LastNameValidator lastNameValidator;
    private final BirthDateValidator birthDateValidator;

    @Override
    public void validate(UserDto user) {
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

package yaremax.com.cs_task_24_04.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.User;

@Service
@RequiredArgsConstructor
public class UserValidator {
    private final EmailValidator emailValidator;
    private final DateValidator dateValidator;

    public User validateFullUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()
                || user.getFirstName() == null ||  user.getFirstName().isEmpty()
                || user.getLastName() == null ||  user.getLastName().isEmpty()
                || user.getBirthDate() == null) throw new InvalidDataException("Invalid data. Email, first name, last name or birth date field are empty");
        dateValidator.validateBirthDate(user.getBirthDate());
        if (!emailValidator.isValid(user.getEmail())) throw new InvalidDataException("Invalid email");

        return user;
    }

    public User validatePartialUser(User partialUser) {
        if (partialUser.getBirthDate() != null) {
            dateValidator.validateBirthDate(partialUser.getBirthDate());
        }
        if (partialUser.getEmail() != null) {
            if (!emailValidator.isValid(partialUser.getEmail())) throw new InvalidDataException("Invalid email");
        }
        return partialUser;
    }
}

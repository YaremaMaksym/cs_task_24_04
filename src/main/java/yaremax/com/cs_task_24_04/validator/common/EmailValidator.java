package yaremax.com.cs_task_24_04.validator.common;

import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.validator.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailValidator implements Validator<String> {
    /*
    VALID
    username@domain.com
    user.name@domain.com
    user22name@domain.com
    12username12@domain.co.in

    NOT VALID
    username.@domain.com
    .username@domain.com
    user-name12@domain.com
    user_name@domain.com
    username@.com
     */
    private static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public void validate(String email)  {
        if (email == null || email.trim().isEmpty()){
            throw new InvalidDataException("Email cannot be null or empty");
        }

        Pattern pattern = Pattern.compile(EMAIL_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) throw new InvalidDataException("Invalid email");
    }
}

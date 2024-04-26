package yaremax.com.cs_task_24_04.validators;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator {
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
    private static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public boolean isValid(String email) {
        if (email == null || email.isEmpty()){
            return false;
        }

        Pattern pattern = Pattern.compile(EMAIL_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

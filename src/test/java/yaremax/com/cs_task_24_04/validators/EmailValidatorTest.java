package yaremax.com.cs_task_24_04.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EmailValidatorTest {

    private final EmailValidator emailValidator = new EmailValidator();

    @ParameterizedTest
    @ValueSource(strings = {"username@domain.com", "user.name@domain.com", "user22name@domain.com", "12username12@domain.co.in"})
    void shouldValidateValidEmails(String email) {
        assertThat(emailValidator.isValid(email)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"username.@domain.com", ".username@domain.com", "user-name12@domain.com", "user_name@domain.com", "username@.com"})
    void shouldInvalidateInvalidEmails(String email) {
        assertThat(emailValidator.isValid(email)).isFalse();
    }

    @Test
    void shouldInvalidateNullEmail() {
        assertThat(emailValidator.isValid(null)).isFalse();
    }

    @Test
    void shouldInvalidateEmptyEmail() {
        assertThat(emailValidator.isValid("")).isFalse();
    }

}
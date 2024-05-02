package yaremax.com.cs_task_24_04.validator.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class EmailValidatorTest {

    private final EmailValidator validator = new EmailValidator();

    @ParameterizedTest
    @NullAndEmptySource
    void validate_NullOrEmptyEmail_ShouldThrowException(String email) {
        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username.@domain.com", ".username@domain.com", "user-name12@domain.com",
            "user_name@domain.com", "username@.com"})
    void validate_InvalidEmailFormat_ShouldThrowException(String email) {
        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username@domain.com", "user.name@domain.com", "user22name@domain.com",
            "12username12@domain.co.in"})
    void validate_ValidEmailFormat_ShouldNotThrowException(String email) {
        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> validator.validate(email));
    }
}
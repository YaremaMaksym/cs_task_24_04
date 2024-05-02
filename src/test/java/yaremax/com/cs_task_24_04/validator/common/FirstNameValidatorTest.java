package yaremax.com.cs_task_24_04.validator.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class FirstNameValidatorTest {

    private final FirstNameValidator validator = new FirstNameValidator();

    @ParameterizedTest
    @NullAndEmptySource
    void validate_NullOrEmptyFirstName_ShouldThrowException(String firstName) {
        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(firstName));
    }

    @Test
    void validate_ValidFirstName_ShouldNotThrowException() {
        // Arrange
        String validFirstName = "John";

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> validator.validate(validFirstName));
    }
}
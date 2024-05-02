package yaremax.com.cs_task_24_04.validator.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class LastNameValidatorTest {

    private final LastNameValidator validator = new LastNameValidator();

    @ParameterizedTest
    @NullAndEmptySource
    void validate_NullOrEmptyLastName_ShouldThrowException(String lastName) {
        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(lastName));
    }

    @Test
    void validate_ValidLastName_ShouldNotThrowException() {
        // Arrange
        String validLastName = "Doe";

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> validator.validate(validLastName));
    }
}
package yaremax.com.cs_task_24_04.validator.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest(properties = {"app.config.minAge=18"})
class BirthDateValidatorTest {

    @Autowired
    private BirthDateValidator validator;

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 42})
    void validate_FutureBirthDate_ShouldThrowException(int yearsInFuture) {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusYears(yearsInFuture);

        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(futureDate));
    }

    @Test
    void validate_TooYoungAge_ShouldThrowException() {
        // Arrange
        LocalDate tooYoungDate = LocalDate.now().minusYears(17).minusMonths(1);

        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(tooYoungDate));
    }

    @Test
    void validate_ValidBirthDate_ShouldNotThrowException() {
        // Arrange
        LocalDate validDate = LocalDate.now().minusYears(20);

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> validator.validate(validDate));
    }
}
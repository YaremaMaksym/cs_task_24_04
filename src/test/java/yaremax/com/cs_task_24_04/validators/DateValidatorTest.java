package yaremax.com.cs_task_24_04.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;

import java.time.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest // because we want to test receiving data from property file
@TestPropertySource(properties = "app.config.minAge=18")
class DateValidatorTest {

    @Autowired
    private DateValidator dateValidator;

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 42})
    void shouldThrowExceptionForFutureBirthDates(int yearsInFuture) {
        // arrange
        LocalDate futureDate = LocalDate.now().plusYears(yearsInFuture);

        // act & assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> dateValidator.validateBirthDate(futureDate));
    }

    @Test
    void shouldThrowExceptionForTooYoungAge() {
        // arrange
        LocalDate tooYoungDate = LocalDate.now().minusYears(17).minusMonths(1);

        // act & assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> dateValidator.validateBirthDate(tooYoungDate));
    }

    @Test
    void shouldNotThrowExceptionForValidBirthDate() {
        // arrange
        LocalDate validDate = LocalDate.now().minusYears(20);

        // act & assert
        assertThatNoException()
                .isThrownBy(() -> dateValidator.validateBirthDate(validDate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-01-01/2022-12-31", "2024-05-01/2024-04-30"})
    void shouldThrowExceptionForInvalidDateRange(String dateRange) {
        // arrange
        String[] dates = dateRange.split("/");
        LocalDate from = LocalDate.parse(dates[0]);
        LocalDate to = LocalDate.parse(dates[1]);

        // act & assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> dateValidator.validateDateRange(from, to));
    }

    @Test
    void shouldNotThrowExceptionForValidDateRange() {
        // arrange
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(7);

        // act & assert
        assertThatNoException()
                .isThrownBy(() -> dateValidator.validateDateRange(from, to));
    }

}
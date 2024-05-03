package yaremax.com.cs_task_24_04.validator.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.DateRange;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class DateRangeValidatorTest {

    private final DateRangeValidator validator = new DateRangeValidator();

    @Test
    void validate_NullDateRange_ShouldThrowException() {
        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(null));
    }

    @Test
    void validate_NullFromDate_ShouldThrowException() {
        // Arrange
        DateRange dateRange = new DateRange(null, LocalDate.now());

        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(dateRange));
    }

    @Test
    void validate_NullToDate_ShouldThrowException() {
        // Arrange
        DateRange dateRange = new DateRange(LocalDate.now(), null);

        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(dateRange));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-01-01/2022-12-31", "2024-05-01/2024-04-30"})
    void validate_FromDateAfterToDate_ShouldThrowException(String dateRange) {
        // Arrange
        String[] dates = dateRange.split("/");
        LocalDate from = LocalDate.parse(dates[0]);
        LocalDate to = LocalDate.parse(dates[1]);
        DateRange range = new DateRange(from, to);

        // Act & Assert
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(range));
    }

    @Test
    void validate_ValidDateRange_ShouldNotThrowException() {
        // Arrange
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(7);
        DateRange dateRange = new DateRange(from, to);

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> validator.validate(dateRange));
    }
}
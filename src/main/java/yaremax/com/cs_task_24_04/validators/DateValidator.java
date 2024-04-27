package yaremax.com.cs_task_24_04.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;

import java.time.LocalDate;

@Service
public class DateValidator {

    @Value("${app.config.minAge}")
    private int minAge;

    public void validateBirthDate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) throw new InvalidDataException("Invalid birth date. Can't be in future");
        if (LocalDate.now().getYear() - birthDate.getYear() < minAge) throw new InvalidDataException("Invalid birth date. Not old enough (min age is " + minAge + " y.)");
    }

    public void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isBefore(to)) throw new InvalidDataException("Invalid date range. 'to' can't be before 'from'");
    }
}

package yaremax.com.cs_task_24_04.validator.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.validator.Validator;

import java.time.LocalDate;

@Service
public class BirthDateValidator implements Validator<LocalDate> {
    private final Integer minAge;

    public BirthDateValidator(@Value("${app.config.minAge}") Integer minAge) {
        this.minAge = minAge;
    }

    @Override
    public void validate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new InvalidDataException("Birth date cannot be null or empty");
        }

        if (LocalDate.now().isBefore(birthDate)) throw new InvalidDataException("Invalid birth date. Can't be in future");
        if (LocalDate.now().getYear() - birthDate.getYear() < minAge) throw new InvalidDataException("Invalid birth date. Not old enough (min age is " + minAge + " y.)");
    }
}

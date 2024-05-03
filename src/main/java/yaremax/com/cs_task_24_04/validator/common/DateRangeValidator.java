package yaremax.com.cs_task_24_04.validator.common;

import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.DateRange;
import yaremax.com.cs_task_24_04.validator.Validator;

import java.time.LocalDate;

@Component
public class DateRangeValidator implements Validator<DateRange> {
    @Override
    public void validate(DateRange dateRange) {
        if (dateRange == null) {
            throw new InvalidDataException("Date range cannot be null");
        }

        LocalDate fromDate = dateRange.fromDate();
        LocalDate toDate = dateRange.toDate();

        if (fromDate == null || toDate == null) {
            throw new InvalidDataException("Both fromDate and toDate dates must be provided");
        }

        if (fromDate.isAfter(toDate)) {
            throw new InvalidDataException("From date cannot be after toDate date");
        }
    }
}

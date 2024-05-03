package yaremax.com.cs_task_24_04.validator.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.UserDto;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class FullUserDtoValidatorTest {

    private final FullUserDtoValidator validator = new FullUserDtoValidator();

    @Test
    void validate_NullUser_ShouldThrowException() {
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(null));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUsers")
    void validate_InvalidUser_ShouldThrowException(UserDto user) {
        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> validator.validate(user));
    }

    @Test
    void validate_ValidUser_ShouldNotThrowException() {
        UserDto validUser = UserDto.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.now().minusYears(25))
                .build();

        assertThatNoException()
                .isThrownBy(() -> validator.validate(validUser));
    }

    private static Stream<UserDto> provideInvalidUsers() {
        return Stream.of(
                UserDto.builder().email(null).firstName("John").lastName("Doe").birthDate(LocalDate.now().minusYears(25)).build(),
                UserDto.builder().email("invalid@email").firstName("John").lastName("Doe").birthDate(LocalDate.now().minusYears(25)).build(),
                UserDto.builder().email("john.doe@example.com").firstName(null).lastName("Doe").birthDate(LocalDate.now().minusYears(25)).build(),
                UserDto.builder().email("john.doe@example.com").firstName("John").lastName(null).birthDate(LocalDate.now().minusYears(25)).build(),
                UserDto.builder().email("john.doe@example.com").firstName("John").lastName("Doe").birthDate(LocalDate.now().plusYears(1)).build()
        );
    }
}
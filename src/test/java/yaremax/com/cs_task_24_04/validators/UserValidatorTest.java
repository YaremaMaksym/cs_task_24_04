package yaremax.com.cs_task_24_04.validators;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserValidatorTest {

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private DateValidator dateValidator;

    @InjectMocks
    private UserValidator userValidator;

    @Nested
    class FullUserValidationTests {
        @Test
        void validateFullUser_AllFieldsValid_ShouldReturnUser() {
            // Arrange
            User user = User.builder()
                    .email("john.doe@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();

            when(emailValidator.isValid(user.getEmail())).thenReturn(true);

            // Act & Assert
            assertThat(userValidator.validateFullUser(user)).isEqualTo(user);
            verify(dateValidator).validateBirthDate(user.getBirthDate());
            verify(emailValidator).isValid(user.getEmail());
        }

        @Test
        void validateFullUser_InvalidEmail_ShouldThrowException() {
            // Arrange
            User user = User.builder()
                    .email("john.@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();
            when(emailValidator.isValid(user.getEmail())).thenReturn(false);

            // Act & Assert
            assertThatExceptionOfType(InvalidDataException.class)
                    .isThrownBy(() -> userValidator.validateFullUser(user));
        }

        @Nested
        class FullUserValidationTests_OneFieldEmptyOrNull {
            @Test
            void validateFullUser_NullEmail_ShouldThrowException() {
                // Arrange
                User user = User.builder()
                        .email(null)
                        .firstName("John")
                        .lastName("Doe")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build();

                // Act & Assert
                assertThatExceptionOfType(InvalidDataException.class)
                        .isThrownBy(() -> userValidator.validateFullUser(user));
                verifyNoInteractions(dateValidator, emailValidator);
            }

            @Test
            void validateFullUser_EmptyEmail_ShouldThrowException() {
                // Arrange
                User user = User.builder()
                        .email("")
                        .firstName("John")
                        .lastName("Doe")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build();

                // Act & Assert
                assertThatExceptionOfType(InvalidDataException.class)
                        .isThrownBy(() -> userValidator.validateFullUser(user));
                verifyNoInteractions(dateValidator, emailValidator);
            }

            @Test
            void validateFullUser_NullFirstName_ShouldThrowException() {
                // Arrange
                User user = User.builder()
                        .email("john.doe@example.com")
                        .firstName(null)
                        .lastName("Doe")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build();

                // Act & Assert
                assertThatExceptionOfType(InvalidDataException.class)
                        .isThrownBy(() -> userValidator.validateFullUser(user));
                verifyNoInteractions(dateValidator, emailValidator);
            }

            @Test
            void validateFullUser_EmptyFirstName_ShouldThrowException() {
                // Arrange
                User user = User.builder()
                        .email("john.doe@example.com")
                        .firstName("")
                        .lastName("Doe")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build();

                // Act & Assert
                assertThatExceptionOfType(InvalidDataException.class)
                        .isThrownBy(() -> userValidator.validateFullUser(user));
                verifyNoInteractions(dateValidator, emailValidator);
            }

            @Test
            void validateFullUser_NullLastName_ShouldThrowException() {
                // Arrange
                User user = User.builder()
                        .email("john.doe@example.com")
                        .firstName("John")
                        .lastName(null)
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build();

                // Act & Assert
                assertThatExceptionOfType(InvalidDataException.class)
                        .isThrownBy(() -> userValidator.validateFullUser(user));
                verifyNoInteractions(dateValidator, emailValidator);
            }

            @Test
            void validateFullUser_EmptyLastName_ShouldThrowException() {
                // Arrange
                User user = User.builder()
                        .email("john.doe@example.com")
                        .firstName("John")
                        .lastName("")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build();

                // Act & Assert
                assertThatExceptionOfType(InvalidDataException.class)
                        .isThrownBy(() -> userValidator.validateFullUser(user));
                verifyNoInteractions(dateValidator, emailValidator);
            }

            @Test
            void validateFullUser_NullBirthDate_ShouldThrowException() {
                // Arrange
                User user = User.builder()
                        .email("john.doe@example.com")
                        .firstName("John")
                        .lastName("Doe")
                        .birthDate(null)
                        .build();

                // Act & Assert
                assertThatExceptionOfType(InvalidDataException.class)
                        .isThrownBy(() -> userValidator.validateFullUser(user));
                verifyNoInteractions(dateValidator, emailValidator);
            }
        }
    }

    @Nested
    class PartialUserValidationTests {
        @Test
        void validatePartialUser_BirthDateNull_ShouldNotValidateBirthDate() {
            // Arrange
            User user = User.builder()
                    .email("john.doe@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .build();
            when(emailValidator.isValid(user.getEmail())).thenReturn(true);

            // Act
            userValidator.validatePartialUser(user);

            // Assert
            assertThatNoException()
                    .isThrownBy(() -> userValidator.validatePartialUser(user));
            verifyNoInteractions(dateValidator);
        }

        @Test
        void validatePartialUser_EmailNull_ShouldNotValidateEmail() {
            // Arrange
            User user = User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();
            when(emailValidator.isValid(user.getEmail())).thenReturn(false);

            // Act
            userValidator.validatePartialUser(user);

            // Assert
            verify(dateValidator).validateBirthDate(user.getBirthDate());
            verifyNoInteractions(emailValidator);
        }

        @Test
        void validatePartialUser_BirthDateValid_EmailInvalid_ShouldThrowException() {
            // Arrange
            User user = User.builder()
                    .email("john.doe.@example.com") // Invalid email
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .build();
            when(emailValidator.isValid(user.getEmail())).thenReturn(false);

            // Act & Assert
            assertThatExceptionOfType(InvalidDataException.class)
                    .isThrownBy(() -> userValidator.validatePartialUser(user));
            verify(dateValidator).validateBirthDate(user.getBirthDate());
            verify(emailValidator).isValid(user.getEmail());
        }

        @Test
        void validatePartialUser_BirthDateNull_EmailValid_ShouldNotThrowException() {
            // Arrange
            User user = User.builder()
                    .email("john.doe@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .build();
            when(emailValidator.isValid(user.getEmail())).thenReturn(true);

            // Act & Assert
            assertThatNoException()
                    .isThrownBy(() -> userValidator.validatePartialUser(user));
        }

        @Test
        void validatePartialUser_AllFieldsNull_ShouldNotThrowException() {
            // Arrange
            User user = User.builder().build();

            // Act & Assert
            assertThatNoException()
                    .isThrownBy(() -> userValidator.validatePartialUser(user));
            verifyNoInteractions(dateValidator);
            verifyNoInteractions(emailValidator);
        }
    }
}

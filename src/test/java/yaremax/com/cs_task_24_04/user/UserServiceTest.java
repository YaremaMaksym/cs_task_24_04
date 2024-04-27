package yaremax.com.cs_task_24_04.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.cs_task_24_04.exceptions.DuplicateResourceException;
import yaremax.com.cs_task_24_04.exceptions.ResourceNotFoundException;
import yaremax.com.cs_task_24_04.validators.DateValidator;
import yaremax.com.cs_task_24_04.validators.UserValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private DateValidator dateValidator;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userValidator, dateValidator);
    }

    @Nested
    class CreateUserTests {
        @Test
        void createUser_shouldCreateUser_whenEmailNotExists() {
            // Arrange
            User user = User.builder()
                    .email("test@example.com")
                    .build();
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
            when(userValidator.validateFullUser(user)).thenReturn(user);
            when(userRepository.save(user)).thenReturn(user);

            // Act
            User createdUser = userService.createUser(user);

            // Assert
            assertThat(createdUser).isSameAs(user);
            verify(userRepository, times(1)).existsByEmail(user.getEmail());
            verify(userValidator, times(1)).validateFullUser(user);
            verify(userRepository, times(1)).save(user);
        }

        @Test
        void createUser_shouldThrowDuplicateResourceException_whenEmailExists() {
            // Arrange
            User user = User.builder()
                    .email("test@example.com")
                    .build();
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> userService.createUser(user));
            verify(userRepository, times(1)).existsByEmail(user.getEmail());
        }
    }

    @Nested
    class GetUserByIdTests {

        @Test
        void getUserById_UserExists_ShouldReturnUser() {
            // Arrange
            long userId = 1L;
            User user = User.builder()
                    .id(userId)
                    .email("test@example.com")
                    .build();
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // Act
            User retrievedUser = userService.getUserById(userId);

            // Assert
            assertThat(retrievedUser).isEqualTo(user);
        }

        @Test
        void getUserById_UserDoesNotExist_ShouldThrowResourceNotFoundException() {
            // Arrange
            long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> userService.getUserById(userId));
        }
    }

    @Nested
    class GetAllUsersTests {

        @Test
        void getAllUsers_ShouldReturnListOfUsers() {
            // Arrange
            List<User> userList = new ArrayList<>();
            userList.add(User.builder().email("test1@example.com").build());
            userList.add(User.builder().email("test2@example.com").build());
            when(userRepository.findAll()).thenReturn(userList);

            // Act
            List<User> retrievedUsers = userService.getAllUsers();

            // Assert
            assertThat(retrievedUsers).isEqualTo(userList);
        }
    }

    @Nested
    class UpdateUserTests {

        @Test
        void updateUser_shouldUpdateUser_whenValidUserProvided() {
            // Arrange
            Long id = 1L;
            User existingUser = User.builder()
                    .id(id)
                    .email("old@example.com")
                    .build();
            User updatedUser = User.builder()
                    .id(id)
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(false);
            when(userValidator.validateFullUser(updatedUser)).thenReturn(updatedUser);
            when(userRepository.save(updatedUser)).thenReturn(updatedUser);

            // Act
            User result = userService.updateUser(id, updatedUser);

            // Assert
            assertThat(result).isEqualTo(updatedUser);
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(updatedUser.getEmail());
            verify(userValidator, times(1)).validateFullUser(updatedUser);
            verify(userRepository, times(1)).save(updatedUser);
        }

        @Test
        void updateUser_shouldThrowResourceNotFoundException_whenUserNotFound() {
            // Arrange
            Long id = 1L;
            User updatedUser = User.builder()
                    .id(id)
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> userService.updateUser(id, updatedUser));
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void updateUser_shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
            // Arrange
            Long id = 1L;
            User existingUser = User.builder()
                    .id(id)
                    .email("old@example.com")
                    .build();
            User updatedUser = User.builder()
                    .id(id)
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(true);

            // Act & Assert

            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> userService.updateUser(id, updatedUser));
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(updatedUser.getEmail());
        }
    }

    @Nested
    class PatchUserTests {
        @Test
        void patchUser_shouldUpdateEmail_whenValidEmailProvided() {
            // Arrange
            Long id = 1L;
            User existingUser = User.builder()
                    .id(id)
                    .email("old@example.com")
                    .build();
            User partialUser = User.builder()
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(partialUser.getEmail())).thenReturn(false);
            when(userValidator.validatePartialUser(partialUser)).thenReturn(partialUser);
            when(userRepository.save(existingUser)).thenReturn(existingUser);

            // Act
            User result = userService.patchUser(id, partialUser);

            // Assert
            assertThat(result).isEqualTo(existingUser);
            assertThat(result.getEmail()).isEqualTo(partialUser.getEmail());
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(partialUser.getEmail());
            verify(userValidator, times(1)).validatePartialUser(partialUser);
            verify(userRepository, times(1)).save(existingUser);
        }

        @Test
        void patchUser_shouldUpdateFirstAndLastName_whenFirstAndLastNameProvided() {
            // Arrange
            Long id = 1L;
            User existingUser = User.builder()
                    .id(id)
                    .firstName("John")
                    .lastName("Doe")
                    .build();
            User partialUser = User.builder()
                    .firstName("Bob")
                    .lastName("Smite")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userValidator.validatePartialUser(partialUser)).thenReturn(partialUser);
            when(userRepository.save(existingUser)).thenReturn(existingUser);

            // Act
            User result = userService.patchUser(id, partialUser);

            // Assert
            assertThat(result).isEqualTo(existingUser);
            assertThat(result.getFirstName()).isEqualTo(partialUser.getFirstName());
            verify(userRepository, times(1)).findById(id);
            verify(userValidator, times(1)).validatePartialUser(partialUser);
            verify(userRepository, times(1)).save(existingUser);
        }

        @Test
        void patchUser_shouldThrowResourceNotFoundException_whenUserNotFound() {
            // Arrange
            Long id = 1L;
            User partialUser = User.builder()
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> userService.patchUser(id, partialUser));
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void patchUser_shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
            // Arrange
            Long id = 1L;
            User existingUser = User.builder()
                    .id(id)
                    .email("old@example.com")
                    .build();
            User partialUser = User.builder()
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(partialUser.getEmail())).thenReturn(true);
            when(userValidator.validatePartialUser(partialUser)).thenReturn(partialUser);

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> userService.patchUser(id, partialUser));
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(partialUser.getEmail());
        }
    }

    @Nested
    class DeleteUserTests {

        @Test
        void deleteUser_UserExists_ShouldDeleteUser() {
            // Arrange
            long userId = 1L;
            when(userRepository.existsById(userId)).thenReturn(true);

            // Act
            userService.deleteUser(userId);

            // Assert
            verify(userRepository).deleteById(userId);
        }

        @Test
        void deleteUser_UserDoesNotExist_ShouldThrowResourceNotFoundException() {
            // Arrange
            long userId = 1L;
            when(userRepository.existsById(userId)).thenReturn(false);

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> userService.deleteUser(userId));
        }
    }

    @Nested
    class GetAllUsersByBirthDateRangeTests {

        @Test
        void getAllUsersByBirthDateRange_ValidRange_ShouldReturnListOfUsers() {
            // Arrange
            LocalDate fromDate = LocalDate.of(2020, 1, 1);
            LocalDate toDate = LocalDate.of(2020, 12, 31);
            List<User> userList = new ArrayList<>();
            userList.add(User.builder().email("test1@example.com").build());
            userList.add(User.builder().email("test2@example.com").build());
            when(userRepository.findUsersByBirthDateBetween(fromDate, toDate)).thenReturn(userList);

            // Act
            List<User> retrievedUsers = userService.getAllUsersByBirthDateRange(fromDate, toDate);

            // Assert
            assertThat(retrievedUsers).isEqualTo(userList);
        }

        @Test
        void getAllUsersByBirthDateRange_NoUsersInSpecifiedRange_ShouldReturnEmptyList() {
            // Arrange
            LocalDate fromDate = LocalDate.of(2020, 1, 1);
            LocalDate toDate = LocalDate.of(2020, 12, 31);
            when(userRepository.findUsersByBirthDateBetween(fromDate, toDate)).thenReturn(Collections.emptyList());

            // Act
            List<User> retrievedUsers = userService.getAllUsersByBirthDateRange(fromDate, toDate);

            // Assert
            assertThat(retrievedUsers).isEmpty();
        }
    }


}
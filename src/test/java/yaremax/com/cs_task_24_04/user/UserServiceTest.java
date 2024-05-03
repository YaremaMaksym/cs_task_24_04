package yaremax.com.cs_task_24_04.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.cs_task_24_04.exceptions.DuplicateResourceException;
import yaremax.com.cs_task_24_04.exceptions.ResourceNotFoundException;
import yaremax.com.cs_task_24_04.mappers.UserMapper;
import yaremax.com.cs_task_24_04.validator.common.DateRangeValidator;
import yaremax.com.cs_task_24_04.validator.user.FullUserDtoValidator;
import yaremax.com.cs_task_24_04.validator.user.PartialUserDtoValidator;

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
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FullUserDtoValidator fullUserDtoValidator;
    @Mock
    private PartialUserDtoValidator partialUserDtoValidator;
    @Mock
    private DateRangeValidator dateRangeValidator;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userMapper, userRepository, fullUserDtoValidator, partialUserDtoValidator, dateRangeValidator);
    }

    @Nested
    class CreateUserTests {
        @Test
        void createUser_shouldCreateUser_whenEmailNotExists() {
            // Arrange
            UserDto userDto = UserDto.builder()
                    .email("test@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
                    .build();
            User user = User.builder()
                    .email("test@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
                    .build();
            when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
            doNothing().when(fullUserDtoValidator).validate(userDto);
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toEntity(userDto)).thenReturn(user);

            // Act
            User createdUser = userService.createUser(userDto);

            // Assert
            assertThat(createdUser).isSameAs(user);
            verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
            verify(fullUserDtoValidator, times(1)).validate(userDto);
            verify(userRepository, times(1)).save(user);
            verify(userMapper, times(1)).toEntity(userDto);
        }

        @Test
        void createUser_shouldThrowDuplicateResourceException_whenEmailExists() {
            // Arrange
            UserDto userDto = UserDto.builder()
                    .email("test@example.com")
                    .build();
            when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> userService.createUser(userDto));
            verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
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
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
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
            userList.add(User.builder().email("test1@example.com").firstName("John").lastName("Doe").birthDate(LocalDate.now().minusYears(25)).build());
            userList.add(User.builder().email("test2@example.com").firstName("Jane").lastName("Doe").birthDate(LocalDate.now().minusYears(30)).build());
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
            UserDto updatedUserDto = UserDto.builder()
                    .email("new@example.com")
                    .firstName("Jane")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(30))
                    .build();
            User existingUser = User.builder()
                    .id(id)
                    .email("old@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
                    .build();
            User updatedUser = User.builder()
                    .id(id)
                    .email("new@example.com")
                    .firstName("Jane")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(30))
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(updatedUserDto.getEmail())).thenReturn(false);
            doNothing().when(fullUserDtoValidator).validate(updatedUserDto);
            when(userRepository.save(updatedUser)).thenReturn(updatedUser);

            // Act
            User result = userService.updateUser(id, updatedUserDto);

            // Assert
            assertThat(result).isEqualTo(updatedUser);
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(updatedUserDto.getEmail());
            verify(fullUserDtoValidator, times(1)).validate(updatedUserDto);
            verify(userRepository, times(1)).save(updatedUser);
        }

        @Test
        void updateUser_shouldThrowResourceNotFoundException_whenUserNotFound() {
            // Arrange
            Long id = 1L;
            UserDto updatedUserDto = UserDto.builder()
                    .email("new@example.com")
                    .firstName("Jane")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(30))
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> userService.updateUser(id, updatedUserDto));
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void updateUser_shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
            // Arrange
            Long id = 1L;
            UserDto updatedUserDto = UserDto.builder()
                    .email("new@example.com")
                    .firstName("Jane")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(30))
                    .build();
            User existingUser = User.builder()
                    .id(id)
                    .email("old@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(updatedUserDto.getEmail())).thenReturn(true);

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> userService.updateUser(id, updatedUserDto));
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(updatedUserDto.getEmail());
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
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
                    .build();
            UserDto partialUserDto = UserDto.builder()
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(partialUserDto.getEmail())).thenReturn(false);
            doNothing().when(partialUserDtoValidator).validate(partialUserDto);
            when(userRepository.save(existingUser)).thenReturn(existingUser);

            // Act
            User result = userService.patchUser(id, partialUserDto);

            // Assert
            assertThat(result).isEqualTo(existingUser);
            assertThat(result.getEmail()).isEqualTo(partialUserDto.getEmail());
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(partialUserDto.getEmail());
            verify(partialUserDtoValidator, times(1)).validate(partialUserDto);
            verify(userRepository, times(1)).save(existingUser);
        }

        @Test
        void patchUser_shouldUpdateFirstAndLastName_whenFirstAndLastNameProvided() {
            // Arrange
            Long id = 1L;
            User existingUser = User.builder()
                    .id(id)
                    .email("john@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
                    .build();
            UserDto partialUserDto = UserDto.builder()
                    .firstName("Bob")
                    .lastName("Smite")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            doNothing().when(partialUserDtoValidator).validate(partialUserDto);
            when(userRepository.save(existingUser)).thenReturn(existingUser);

            // Act
            User result = userService.patchUser(id, partialUserDto);

            // Assert
            assertThat(result).isEqualTo(existingUser);
            assertThat(result.getFirstName()).isEqualTo(partialUserDto.getFirstName());
            assertThat(result.getLastName()).isEqualTo(partialUserDto.getLastName());
            verify(userRepository, times(1)).findById(id);
            verify(partialUserDtoValidator, times(1)).validate(partialUserDto);
            verify(userRepository, times(1)).save(existingUser);
        }

        @Test
        void patchUser_shouldThrowResourceNotFoundException_whenUserNotFound() {
            // Arrange
            Long id = 1L;
            UserDto partialUserDto = UserDto.builder()
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> userService.patchUser(id, partialUserDto));
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void patchUser_shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
            // Arrange
            Long id = 1L;
            User existingUser = User.builder()
                    .id(id)
                    .email("old@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.now().minusYears(25))
                    .build();
            UserDto partialUserDto = UserDto.builder()
                    .email("new@example.com")
                    .build();
            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(partialUserDto.getEmail())).thenReturn(true);
            doNothing().when(partialUserDtoValidator).validate(partialUserDto);

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> userService.patchUser(id, partialUserDto));
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).existsByEmail(partialUserDto.getEmail());
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
            userList.add(User.builder().email("test1@example.com").firstName("John").lastName("Doe").birthDate(LocalDate.of(2020, 6, 15)).build());
            userList.add(User.builder().email("test2@example.com").firstName("Jane").lastName("Smith").birthDate(LocalDate.of(2020, 10, 20)).build());
            when(userRepository.findUsersByBirthDateBetween(fromDate, toDate)).thenReturn(userList);
            doNothing().when(dateRangeValidator).validate(any(DateRange.class));

            // Act
            List<User> retrievedUsers = userService.getAllUsersByBirthDateRange(fromDate, toDate);

            // Assert
            assertThat(retrievedUsers).isEqualTo(userList);
            verify(dateRangeValidator).validate(new DateRange(fromDate, toDate));
        }

        @Test
        void getAllUsersByBirthDateRange_NoUsersInSpecifiedRange_ShouldReturnEmptyList() {
            // Arrange
            LocalDate fromDate = LocalDate.of(2020, 1, 1);
            LocalDate toDate = LocalDate.of(2020, 12, 31);
            when(userRepository.findUsersByBirthDateBetween(fromDate, toDate)).thenReturn(Collections.emptyList());
            doNothing().when(dateRangeValidator).validate(any(DateRange.class));

            // Act
            List<User> retrievedUsers = userService.getAllUsersByBirthDateRange(fromDate, toDate);

            // Assert
            assertThat(retrievedUsers).isEmpty();
            verify(dateRangeValidator).validate(new DateRange(fromDate, toDate));
        }
    }
}
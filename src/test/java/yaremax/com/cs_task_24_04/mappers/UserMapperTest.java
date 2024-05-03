package yaremax.com.cs_task_24_04.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.User;
import yaremax.com.cs_task_24_04.user.UserDto;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    public void toDto_ValidUser_ReturnsCorrectUserDto() {
        // Arrange
        User user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address("123 Main St, Anytown USA")
                .phone("555-1234")
                .build();

        // Act
        UserDto userDto = userMapper.toDto(user);

        // Assert
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getBirthDate(), userDto.getBirthDate());
        assertEquals(user.getAddress(), userDto.getAddress());
        assertEquals(user.getPhone(), userDto.getPhone());
    }

    @Test
    public void toDto_NullUser_ThrowsException() {
        // Act and Assert
        assertThrows(InvalidDataException.class, () -> userMapper.toDto(null));
    }

    @Test
    public void toEntity_ValidUserDto_ReturnsCorrectUser() {
        // Arrange
        UserDto userDto = UserDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address("123 Main St, Anytown USA")
                .phone("555-1234")
                .build();

        // Act
        User user = userMapper.toEntity(userDto);

        // Assert
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getBirthDate(), user.getBirthDate());
        assertEquals(userDto.getAddress(), user.getAddress());
        assertEquals(userDto.getPhone(), user.getPhone());
    }

    @Test
    public void toEntity_NullUserDto_ThrowsException() {
        // Act and Assert
        assertThrows(InvalidDataException.class, () -> userMapper.toEntity(null));
    }
}
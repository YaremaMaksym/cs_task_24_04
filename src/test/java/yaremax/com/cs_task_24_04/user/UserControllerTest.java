package yaremax.com.cs_task_24_04.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yaremax.com.cs_task_24_04.exceptions.GlobalExceptionHandler;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    class GetUserByIdTests {

        @Test
        void getUserById_UserExists_ShouldReturnUser() throws Exception {
            // Arrange
            long userId = 1L;
            User user = new User();
            when(userService.getUserById(userId)).thenReturn(user);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId()));

            verify(userService, times(1)).getUserById(userId);
        }

        @Test
        void getUserById_UserNotFound_ShouldReturnNotFound() throws Exception {
            // Arrange
            long userId = 1L;
            doThrow(new ResourceNotFoundException(""))
                    .when(userService).getUserById(userId);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{id}", userId))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).getUserById(userId);
        }
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Arrange
        List<User> users = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsersByBirthDateRange_ShouldReturnListOfUsers() throws Exception {
        // Arrange
        LocalDate from = LocalDate.now().minusYears(30);
        LocalDate to = LocalDate.now().minusYears(20);
        List<User> users = new ArrayList<>();
        when(userService.getAllUsersByBirthDateRange(from, to)).thenReturn(users);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/period")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService, times(1)).getAllUsersByBirthDateRange(from, to);
    }

    @Nested
    class CreateUserTests {

        @Test
        void createUser_ShouldReturnCreatedUser() throws Exception {
            // Arrange
            User user = User.builder()
                    .email("test@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(2020, 5, 15))
                    .build();

            when(userService.createUser(any())).thenReturn(user);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@example.com",
                                "firstName": "John",
                                "lastName": "Doe",
                                "birthDate": "2020-05-15"
                            }
                            """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId()));

            verify(userService, times(1)).createUser(any());
        }

        @Test
        void createUser_InvalidEmail_ShouldReturnBadRequest() throws Exception {
            // Arrange
            doThrow(new InvalidDataException(""))
                    .when(userService).createUser(any());

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "invalid-email",
                                "firstName": "John",
                                "lastName": "Doe",
                                "birthDate": "2020-05-15"
                            }
                            """))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateUserTests {

        @Test
        void updateUser_ShouldReturnUpdatedUser() throws Exception {
            // Arrange
            long userId = 1L;
            User user = User.builder()
                    .email("test@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .birthDate(LocalDate.of(2020, 5, 15))
                    .build();

            when(userService.updateUser(eq(userId), any())).thenReturn(user);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@example.com",
                                "firstName": "John",
                                "lastName": "Doe",
                                "birthDate": "2020-05-15"
                            }
                            """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId()));

            verify(userService, times(1)).updateUser(eq(userId), any());
        }

        @Test
        void updateUser_UserNotFound_ShouldReturnNotFound() throws Exception {
            // Arrange
            long userId = 1L;
            doThrow(new ResourceNotFoundException(""))
                    .when(userService).updateUser(eq(userId), any());

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@example.com",
                                "firstName": "John",
                                "lastName": "Doe",
                                "birthDate": "2020-05-15"
                            }
                            """))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).updateUser(eq(userId), any());
        }
    }

    @Nested
    class PatchUserTests {

        @Test
        void patchUser_ShouldReturnPatchedUser() throws Exception {
            // Arrange
            long userId = 1L;
            User user = User.builder()
                    .email("test@example.com")
                    .firstName("John")
                    .build();

            when(userService.patchUser(eq(userId), any())).thenReturn(user);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@example.com",
                                "firstName": "John"
                            }
                            """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId()));

            verify(userService, times(1)).patchUser(eq(userId), any());
        }

        @Test
        void patchUser_UserNotFound_ShouldReturnNotFound() throws Exception {
            // Arrange
            long userId = 1L;
            doThrow(new ResourceNotFoundException(""))
                    .when(userService).patchUser(eq(userId), any());

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "email": "test@example.com",
                                "firstName": "John"
                            }
                            """))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).patchUser(eq(userId), any());
        }
    }

    @Nested
    class DeleteUserTests {

        @Test
        void deleteUser_ShouldReturnSuccessMessage() throws Exception {
            // Arrange
            long userId = 1L;

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Successfully deleted user"));

            verify(userService, times(1)).deleteUser(userId);
        }

        @Test
        void deleteUser_UserNotFound_ShouldReturnNotFound() throws Exception {
            // Arrange
            long userId = 1L;
            doThrow(new ResourceNotFoundException(""))
                    .when(userService).deleteUser(userId);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", userId))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).deleteUser(userId);
        }
    }

}
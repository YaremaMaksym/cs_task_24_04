package yaremax.com.cs_task_24_04.user;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()));

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
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

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
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()));

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
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()));

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
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

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
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()));

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
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

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
                    .andExpect(MockMvcResultMatchers.status().isOk())
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
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            verify(userService, times(1)).deleteUser(userId);
        }
    }

}
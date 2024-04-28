package yaremax.com.cs_task_24_04.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmail_ExistingEmail_ShouldReturnTrue() {
        // Arrange
        User existingUser = new User(null, "test@example.com", "John", "Doe", LocalDate.of(2020, 1, 1), null, null);
        userRepository.save(existingUser);

        // Act
        boolean existsByEmail = userRepository.existsByEmail("test@example.com");

        // Assert
        assertThat(existsByEmail).isTrue();
    }

    @Test
    void existsByEmail_NonExistingEmail_ShouldReturnFalse() {
        // Act
        boolean existsByEmail = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertThat(existsByEmail).isFalse();
    }

    @Test
    void findUsersByBirthDateBetween_ValidRange_ShouldReturnUsers() {
        // Arrange
        User user1 = new User(null, "john@example.com", "John", "Doe", LocalDate.of(2015, 1, 1), null, null);
        User user2 = new User(null, "jane@example.com", "Jane", "Doe", LocalDate.of(2020, 5, 15), null, null);
        userRepository.saveAll(List.of(user1, user2));

        // Act
        List<User> users = userRepository.findUsersByBirthDateBetween(LocalDate.of(2000, 1, 1), LocalDate.of(2021, 1, 1));

        // Assert
        assertThat(users).containsExactlyInAnyOrder(user1, user2);
    }
}
package yaremax.com.cs_task_24_04.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaremax.com.cs_task_24_04.exceptions.DuplicateResourceException;
import yaremax.com.cs_task_24_04.exceptions.ResourceNotFoundException;
import yaremax.com.cs_task_24_04.validators.DateValidator;
import yaremax.com.cs_task_24_04.validators.UserValidator;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final DateValidator dateValidator;

    public User createUser(User user) {
        User validUser = userValidator.validateFullUser(user);
        if (userRepository.existsByEmail(user.getEmail())) throw new DuplicateResourceException("User with email " + user.getEmail() + " already exists");
        return userRepository.save(validUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        User validUpdatedUser = userValidator.validateFullUser(updatedUser);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (!existingUser.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) throw new DuplicateResourceException("Email " + updatedUser.getEmail() + " already occupied");

        return userRepository.save(validUpdatedUser);
    }

    public User patchUser(Long id, User partialUser) {
        User validPartialUser = userValidator.validatePartialUser(partialUser);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        if (validPartialUser.getEmail() != null) {
            if (!existingUser.getEmail().equals(validPartialUser.getEmail())
                    && userRepository.existsByEmail(validPartialUser.getEmail())) throw new DuplicateResourceException("Email " + validPartialUser.getEmail() + " already occupied");
            existingUser.setEmail(validPartialUser.getEmail());
        }
        if (validPartialUser.getFirstName() != null) {
            existingUser.setFirstName(validPartialUser.getFirstName());
        }
        if (validPartialUser.getLastName() != null) {
            existingUser.setLastName(validPartialUser.getLastName());
        }
        if (validPartialUser.getBirthDate() != null) {
            existingUser.setBirthDate(validPartialUser.getBirthDate());
        }
        if (validPartialUser.getAddress() != null) {
            existingUser.setAddress(validPartialUser.getAddress());
        }
        if (validPartialUser.getPhone() != null) {
            existingUser.setPhone(validPartialUser.getPhone());
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User with id " + id + " not found");
        userRepository.deleteById(id);
    }

    public List<User> getAllUsersByBirthDateRange(LocalDate from, LocalDate to) {
        dateValidator.validateDateRange(from, to);
        return userRepository.findUsersByBirthDateBetween(from, to);
    }
}

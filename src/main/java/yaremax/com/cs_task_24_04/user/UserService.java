package yaremax.com.cs_task_24_04.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaremax.com.cs_task_24_04.exceptions.DuplicateResourceException;
import yaremax.com.cs_task_24_04.exceptions.ResourceNotFoundException;
import yaremax.com.cs_task_24_04.validator.common.DateRangeValidator;
import yaremax.com.cs_task_24_04.validator.user.FullUserValidator;
import yaremax.com.cs_task_24_04.validator.user.PartialUserValidator;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FullUserValidator fullUserValidator;
    private final PartialUserValidator partialUserValidator;
    private final DateRangeValidator dateRangeValidator;

    public User createUser(User user) {
        fullUserValidator.validate(user);
        if (userRepository.existsByEmail(user.getEmail())) throw new DuplicateResourceException("User with email " + user.getEmail() + " already exists");
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        fullUserValidator.validate(updatedUser);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (!existingUser.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) throw new DuplicateResourceException("Email " + updatedUser.getEmail() + " already occupied");

        return userRepository.save(updatedUser);
    }

    public User patchUser(Long id, User partialUser) {
        partialUserValidator.validate(partialUser);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        if (partialUser.getEmail() != null) {
            if (!existingUser.getEmail().equals(partialUser.getEmail())
                    && userRepository.existsByEmail(partialUser.getEmail())) throw new DuplicateResourceException("Email " + partialUser.getEmail() + " already occupied");
            existingUser.setEmail(partialUser.getEmail());
        }
        if (partialUser.getFirstName() != null) {
            existingUser.setFirstName(partialUser.getFirstName());
        }
        if (partialUser.getLastName() != null) {
            existingUser.setLastName(partialUser.getLastName());
        }
        if (partialUser.getBirthDate() != null) {
            existingUser.setBirthDate(partialUser.getBirthDate());
        }
        if (partialUser.getAddress() != null) {
            existingUser.setAddress(partialUser.getAddress());
        }
        if (partialUser.getPhone() != null) {
            existingUser.setPhone(partialUser.getPhone());
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User with id " + id + " not found");
        userRepository.deleteById(id);
    }

    public List<User> getAllUsersByBirthDateRange(LocalDate from, LocalDate to) {
        dateRangeValidator.validate(new DateRange(from, to));
        return userRepository.findUsersByBirthDateBetween(from, to);
    }
}

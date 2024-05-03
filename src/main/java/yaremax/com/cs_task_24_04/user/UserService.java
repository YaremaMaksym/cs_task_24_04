package yaremax.com.cs_task_24_04.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaremax.com.cs_task_24_04.exceptions.DuplicateResourceException;
import yaremax.com.cs_task_24_04.exceptions.ResourceNotFoundException;
import yaremax.com.cs_task_24_04.mappers.UserMapper;
import yaremax.com.cs_task_24_04.validator.common.DateRangeValidator;
import yaremax.com.cs_task_24_04.validator.user.FullUserDtoValidator;
import yaremax.com.cs_task_24_04.validator.user.PartialUserDtoValidator;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final FullUserDtoValidator fullUserDtoValidator;
    private final PartialUserDtoValidator partialUserDtoValidator;
    private final DateRangeValidator dateRangeValidator;

    public User createUser(UserDto userDto) {
        fullUserDtoValidator.validate(userDto);
        if (userRepository.existsByEmail(userDto.getEmail())) throw new DuplicateResourceException("User with email " + userDto.getEmail() + " already exists");
        return userRepository.save(userMapper.toEntity(userDto));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, UserDto updatedUserDto) {
        fullUserDtoValidator.validate(updatedUserDto);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (!existingUser.getEmail().equals(updatedUserDto.getEmail())
                && userRepository.existsByEmail(updatedUserDto.getEmail())) throw new DuplicateResourceException("Email " + updatedUserDto.getEmail() + " already occupied");

        existingUser.setEmail(updatedUserDto.getEmail());
        existingUser.setFirstName(updatedUserDto.getFirstName());
        existingUser.setLastName(updatedUserDto.getLastName());
        existingUser.setBirthDate(updatedUserDto.getBirthDate());


        if (updatedUserDto.getAddress() != null) {
            existingUser.setAddress(existingUser.getAddress());
        }
        if (updatedUserDto.getPhone() != null) {
            existingUser.setPhone(existingUser.getPhone());
        }

        return userRepository.save(existingUser);
    }

    public User patchUser(Long id, UserDto partialUserDto) {
        partialUserDtoValidator.validate(partialUserDto);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        if (partialUserDto.getEmail() != null) {
            if (!existingUser.getEmail().equals(partialUserDto.getEmail())
                    && userRepository.existsByEmail(partialUserDto.getEmail())) throw new DuplicateResourceException("Email " + partialUserDto.getEmail() + " already occupied");
            existingUser.setEmail(partialUserDto.getEmail());
        }
        if (partialUserDto.getFirstName() != null) {
            existingUser.setFirstName(partialUserDto.getFirstName());
        }
        if (partialUserDto.getLastName() != null) {
            existingUser.setLastName(partialUserDto.getLastName());
        }
        if (partialUserDto.getBirthDate() != null) {
            existingUser.setBirthDate(partialUserDto.getBirthDate());
        }
        if (partialUserDto.getAddress() != null) {
            existingUser.setAddress(partialUserDto.getAddress());
        }
        if (partialUserDto.getPhone() != null) {
            existingUser.setPhone(partialUserDto.getPhone());
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

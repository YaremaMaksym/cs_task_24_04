package yaremax.com.cs_task_24_04.mappers;

import org.springframework.stereotype.Component;
import yaremax.com.cs_task_24_04.exceptions.InvalidDataException;
import yaremax.com.cs_task_24_04.user.User;
import yaremax.com.cs_task_24_04.user.UserDto;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public UserDto toDto(User user) {
        if (user == null) {
            throw new InvalidDataException("User is null");
        }

        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phone(user.getPhone())
                .build();
    }

    @Override
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            throw new InvalidDataException("UserDto is null");
        }

        return User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .birthDate(userDto.getBirthDate())
                .address(userDto.getAddress())
                .phone(userDto.getPhone())
                .build();
    }
}


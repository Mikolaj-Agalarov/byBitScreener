package cryptoDOM.mapper;

import cryptoDOM.dto.UserDtoRequest;
import cryptoDOM.dto.UserDtoResponse;
import cryptoDOM.model.User;

public class UserMapper {
    public static User fromDTO(User user, UserDtoRequest userDto){
        user.setEmail(user.getEmail());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        return user;
    }

    public static UserDtoResponse fromEntity(User entity){
        UserDtoResponse user = new UserDtoResponse();
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setUsername(entity.getUsername());
        user.setRole(entity.getRole());
        user.setId(entity.getId());
        return user;
    }
}

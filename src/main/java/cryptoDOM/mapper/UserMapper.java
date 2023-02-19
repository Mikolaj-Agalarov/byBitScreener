package cryptoDOM.mapper;

import cryptoDOM.dto.UserDTO;
import cryptoDOM.entity.User;

public class UserMapper {
    public static User fromUserDTOtoUser (UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    public static UserDTO fromUserToUserDTO (User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}

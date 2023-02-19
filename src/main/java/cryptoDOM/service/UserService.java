package cryptoDOM.service;

import cryptoDOM.dto.UserDTO;
import cryptoDOM.entity.User;
import cryptoDOM.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cryptoDOM.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void saveUser(UserDTO userDTO) {

        userRepository.save(UserMapper.fromUserDTOtoUser(userDTO));
    }

    public List<UserDTO> findAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::fromUserToUserDTO)
                .toList();
    }

    public boolean isValidEmailAndPassword(String email,String password) {
        return userRepository.findByEmailAndPassword(email,password).isPresent();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

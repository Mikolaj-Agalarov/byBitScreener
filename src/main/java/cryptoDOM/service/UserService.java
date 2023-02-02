package cryptoDOM.service;

import cryptoDOM.dto.UserDtoRequest;
import cryptoDOM.dto.UserDtoResponse;
import cryptoDOM.mapper.UserMapper;
import cryptoDOM.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cryptoDOM.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDtoResponse saveUser(UserDtoRequest userDto) {
        return UserMapper.fromEntity(
                userRepository.save(
                    UserMapper.fromDTO(new User(),userDto)
            )
        );
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserDtoResponse> findAll() {
        return userRepository.findAll().stream().map(UserMapper::fromEntity).collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

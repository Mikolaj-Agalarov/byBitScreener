package cryptoDOM.service;

import cryptoDOM.dto.UserDTO;
import cryptoDOM.entity.Role;
import cryptoDOM.entity.User;
import cryptoDOM.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cryptoDOM.repository.UserRepository;

import javax.naming.AuthenticationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public void registerNewUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setId(2L);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);
        userRepository.save(user);
    }

    public User authenticateUser(String username, String password) throws AuthenticationException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthenticationException("Invalid username or password");
        }

        if (!passwordEncoder().matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return user;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

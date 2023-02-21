package cryptoDOM.repository;

import cryptoDOM.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email=?1 and u.password=?2")
    Optional<User> findByEmailAndPassword(String email, String password);

    @Query("select u from User u where u.username=?1")
    User findByUsername(String username);

}


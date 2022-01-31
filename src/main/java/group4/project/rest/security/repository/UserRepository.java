package group4.project.rest.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import group4.project.rest.security.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByAuthorities_id(Long id);
}

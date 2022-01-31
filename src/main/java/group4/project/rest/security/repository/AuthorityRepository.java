package group4.project.rest.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import group4.project.rest.security.entity.Authority;
import group4.project.rest.security.entity.AuthorityName;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityName input);
}

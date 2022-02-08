package group4.project.rest.controller;

import group4.project.rest.security.entity.Authority;
import group4.project.rest.security.entity.AuthorityName;
import group4.project.rest.security.entity.User;
import group4.project.rest.security.repository.AuthorityRepository;
import group4.project.rest.security.repository.UserRepository;
import group4.project.rest.util.LabMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RegisterController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    List<Authority> userAuth = new ArrayList<>();
    Authority authority = new Authority();

    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        User isRegis = userRepository.findByUsername(user.getUsername());
        if (isRegis == null) {
            authority.setName(AuthorityName.ROLE_USER);
            userAuth.add(authority);
//            user.getImageUrl().add(user.getImageUrl().get(0));
            String password = encoder.encode(user.getPassword());
            user.setPassword(password);
            user.setBalance(60.0);
            user.setEnabled(true);
            user.setCoin(0);
            user.getAuthorities().add(authorityRepository.findById(1L).get());
            User newUser = userRepository.save(user);
            return ResponseEntity.ok(LabMapper.INSTANCE.getRegisterDto(newUser));
        } else {
            return ResponseEntity.ok(false);
        }
    }
}

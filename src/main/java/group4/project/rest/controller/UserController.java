package group4.project.rest.controller;


import group4.project.rest.security.entity.User;
import group4.project.rest.security.repository.UserRepository;
import group4.project.rest.util.LabMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserProfileById(@PathVariable("id") Long id){
        User user = userRepository.findById(id).get();
        return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(user));
    }

    @PutMapping("/user/{id}/sell")
    public ResponseEntity<?> sellCoin(@PathVariable("id") Long id){
        User user = userRepository.findById(id).get();
        user.setCoin(user.getCoin()-1);
        final User updateCoins = userRepository.save(user);
        return ResponseEntity.ok(updateCoins);
    }
}

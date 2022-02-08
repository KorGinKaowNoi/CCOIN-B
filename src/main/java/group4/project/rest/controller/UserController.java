package group4.project.rest.controller;


import group4.project.rest.entity.CamtCoin;
import group4.project.rest.entity.StudentBuyCoinDTO;
import group4.project.rest.entity.StudentSellCoinDTO;
import group4.project.rest.repository.CamtCoinRepository;
import group4.project.rest.security.entity.User;
import group4.project.rest.security.repository.UserRepository;
import group4.project.rest.util.LabMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CamtCoinRepository camtCoinRepository;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserProfileById(@PathVariable("id") Long id){
        User user = userRepository.findById(id).get();
        return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(user));
    }

    @PostMapping("/user/{id}/sell")
    public ResponseEntity<?> sellCoin(@PathVariable("id") Long id, @RequestBody StudentSellCoinDTO seller){
        User user = userRepository.findById(id).get();
        if (user.getCoin() <= 5){
            return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(user));
        }else {
            user.setCoin(user.getCoin()-seller.getCoin());
            Double total = seller.getPrice() * seller.getCoin();
            user.setBalance(user.getBalance()+total);
            userRepository.save(user);
        }

        return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(user));
    }

    @PostMapping("/user/{id}/buy")
    public ResponseEntity<?> buyCoin(@PathVariable("id") Long id, @RequestBody StudentBuyCoinDTO buyer){
        User user = userRepository.findById(id).get();
        CamtCoin coin = camtCoinRepository.getById(1L);
        Integer coinLeft = coin.getCoinPerDay();
        Double price = buyer.getAmount()*10.0;
        if(user.getBalance()<price || buyer.getAmount() > coinLeft){
            return ResponseEntity.ok(false);
        }
        user.setBalance(user.getBalance()-price);
        user.setCoin(user.getCoin()+buyer.getAmount());
        coin.setCoinPerDay(coinLeft-buyer.getAmount());
        userRepository.save(user);
        camtCoinRepository.save(coin);
        return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(user));
    }
}

package group4.project.rest.controller;


import group4.project.rest.entity.CamtCoin;
import group4.project.rest.repository.CamtCoinRepository;
import group4.project.rest.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;

@Controller
public class CoinController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CamtCoinRepository camtCoinRepository;

    @GetMapping("/coin/generate")
    public ResponseEntity<?> genCoin(){
        Random rand = new Random();
        Integer amountStudent = userRepository.findAll().size();
        camtCoinRepository.deleteAll();
        CamtCoin number = camtCoinRepository.save(CamtCoin.builder()
                .coinPerDay(rand.nextInt(amountStudent)+1).build());
        return ResponseEntity.ok(number.getCoinPerDay());

    }
}

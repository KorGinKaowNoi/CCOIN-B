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
        CamtCoin number = camtCoinRepository.getById(1L);
        number.setCoinPerDay(rand.nextInt(amountStudent)+1);
        camtCoinRepository.save(number);
        return ResponseEntity.ok(number.getCoinPerDay());

    }

    @GetMapping("/coin/amount")
    public ResponseEntity<?> checkCoin(){
        Integer number = camtCoinRepository.getById(1L).getCoinPerDay();
        return ResponseEntity.ok(number);
    }
}

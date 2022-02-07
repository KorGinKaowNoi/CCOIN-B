package group4.project.rest.config;

import group4.project.rest.entity.*;
import group4.project.rest.repository.CamtCoinRepository;
import group4.project.rest.security.entity.Authority;
import group4.project.rest.security.entity.AuthorityName;
import group4.project.rest.security.entity.User;
import group4.project.rest.security.repository.AuthorityRepository;
import group4.project.rest.security.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Component
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CamtCoinRepository camtCoinRepository;

    @SneakyThrows
    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        addUser();
        Random rand = new Random();
        CamtCoin camtCoin;
        int amountStudent = userRepository.findAll().size();
        camtCoin = camtCoinRepository.save(CamtCoin.builder()
                .coinPerDay(rand.nextInt(amountStudent)+1).build());
    }

    User user1, user2, user3;
    private void addUser() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Authority authUser = Authority.builder().name(AuthorityName.ROLE_USER).build();

        user1 = User.builder()
                .username("user1")
                .password(encoder.encode("user1"))
                .balance(50.0)
                .coin(10)
                .email("user1@gmail.com")
                .enabled(true)
                .lastPasswordResetDate(Date.from(LocalDate.of(2021, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();

        user2 = User.builder()
                .username("user2")
                .password(encoder.encode("user2"))
                .balance(50.0)
                .coin(20)
                .email("user2@gmail.com")
                .enabled(true)
                .lastPasswordResetDate(Date.from(LocalDate.of(2021, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();

        user3 = User.builder()
                .username("user3")
                .password(encoder.encode("user3"))
                .balance(50.0)
                .coin(0)
                .email("user3@gmail.com")
                .enabled(true)
                .lastPasswordResetDate(Date.from(LocalDate.of(2021, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();

        authorityRepository.save(authUser);

        user1.getAuthorities().add(authUser);
        user2.getAuthorities().add(authUser);
        user3.getAuthorities().add(authUser);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}

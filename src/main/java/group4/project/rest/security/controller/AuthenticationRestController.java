package group4.project.rest.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import group4.project.rest.security.JwtTokenUtil;
import group4.project.rest.security.entity.Authority;
import group4.project.rest.security.entity.AuthorityName;
import group4.project.rest.security.entity.JwtUser;
import group4.project.rest.security.entity.User;
import group4.project.rest.security.repository.AuthorityRepository;
import group4.project.rest.security.repository.UserRepository;
import group4.project.rest.util.LabMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("${jwt.route.authentication.path}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        Map result = new HashMap();
        result.put("token", token);
        User user = userRepository.findById(((JwtUser) userDetails).getId()).orElse(null);
        result.put("user", LabMapper.INSTANCE.getUserAuthDTO(user));
        return ResponseEntity.ok(result);
    }


    @GetMapping(value = "${jwt.route.authentication.refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("${jwt.route.authentication.path}/registers")
    public ResponseEntity<?> addUser(@RequestBody User user) throws AuthenticationException {
        if (userRepository.findByUsername(user.getUsername()) == null ) {
            Authority authUser = Authority.builder().name(AuthorityName.ROLE_USER).build();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            authorityRepository.save(authUser);
            user.setEnabled(true);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setLastPasswordResetDate(Date.from(LocalDate.of(2021, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            user.getAuthorities().add(authUser);
            User output = userRepository.save(user);
            Map result = new HashMap();
            result.put("user", LabMapper.INSTANCE.getRegisterDto(output));
            return ResponseEntity.ok(result);
        }else {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.BAD_GATEWAY);
        }
    }
}

package com.aniket.job_alert_system.service;


import com.aniket.job_alert_system.dto.AuthResponse;
import com.aniket.job_alert_system.dto.LoginRequest;
import com.aniket.job_alert_system.dto.RegisterRequest;
import com.aniket.job_alert_system.model.User;
import com.aniket.job_alert_system.repository.UserRepository;
import com.aniket.job_alert_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

//    public AuthResponse register(RegisterRequest request) {
//
//        if(userRepository.existsByEmail(request.getEmail())) {
//            throw  new RuntimeException(("Email already registered"));
//        }
//
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(("User"));
//        user.setPreferences(request.getPreferences());
//
//        userRepository.save(user);
//
//        String token = jwtUtil.generateToken(user.getEmail());
//
//        return new AuthResponse(token,user.getRole(),"Registration Successful");
//    }
        @Value("${app.admin.secret-key}")
        private String adminSecretKey;

        public AuthResponse register(RegisterRequest request) {
            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already registered");
            }

            // Build user object
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPreferences(request.getPreferences());

            // Check admin key
            if (request.getAdminKey() != null &&
                    request.getAdminKey().equals(adminSecretKey)) {
                user.setRole("ADMIN");
            } else {
                user.setRole("USER");
            }

            userRepository.save(user);

            String token = jwtUtil.generateToken(user.getEmail());
            return new AuthResponse(token, user.getRole(), "Registration successful");


    }

    public AuthResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(("User Not Found")));

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getRole(), "Login successful");
    }
}












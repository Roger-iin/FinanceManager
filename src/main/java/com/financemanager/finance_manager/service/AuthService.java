package com.financemanager.finance_manager.service;

import com.financemanager.finance_manager.dto.AuthResponse;
import com.financemanager.finance_manager.dto.LoginRequest;
import com.financemanager.finance_manager.dto.RegisterRequest;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.repository.UserRepository;
import com.financemanager.finance_manager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        UserModel user = UserModel.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), token);
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserModel user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), token);
    }
}

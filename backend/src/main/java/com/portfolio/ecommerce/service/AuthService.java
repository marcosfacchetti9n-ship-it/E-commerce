package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.auth.AuthResponse;
import com.portfolio.ecommerce.dto.auth.LoginRequest;
import com.portfolio.ecommerce.dto.auth.RegisterRequest;
import com.portfolio.ecommerce.entity.Cart;
import com.portfolio.ecommerce.entity.Role;
import com.portfolio.ecommerce.entity.User;
import com.portfolio.ecommerce.exception.BadRequestException;
import com.portfolio.ecommerce.repository.UserRepository;
import com.portfolio.ecommerce.security.CustomUserDetails;
import com.portfolio.ecommerce.security.JwtService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MapperService mapperService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Ya existe un usuario con ese email.");
        }

        User user = new User();
        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(Role.USER));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(new CustomUserDetails(savedUser));
        return mapperService.toAuthResponse(token, savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Credenciales invalidas."));

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return mapperService.toAuthResponse(token, user);
    }
}

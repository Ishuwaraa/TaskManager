package com.treinetic.TaskManager.service;

import com.treinetic.TaskManager.DTO.request.UserLoginDTO;
import com.treinetic.TaskManager.DTO.request.UserRegisterDTO;
import com.treinetic.TaskManager.DTO.response.RefreshTokenResponseDTO;
import com.treinetic.TaskManager.DTO.response.UserLoginResponseDTO;
import com.treinetic.TaskManager.DTO.response.UserRegisterResponseDTO;
import com.treinetic.TaskManager.model.MyUser;
import com.treinetic.TaskManager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JWTService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public UserRegisterResponseDTO register(UserRegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return new UserRegisterResponseDTO(dto.getEmail(), "email already exists");
        }

        MyUser user = new MyUser();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
        if (user.getId() == null) {
            return new UserRegisterResponseDTO(null, "error creating the user");
        }

        return new UserRegisterResponseDTO(user.getEmail(), null);
    }

    public UserLoginResponseDTO login(UserLoginDTO dto) {
        Map<String, Object> claims = new HashMap<>();

        try {
            //authenticating the user
            //this is internally calling the loadUserByUsername func in MyUserDetailsService
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            //fetching again cuz need user id as a claim in the token.
            MyUser user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

            claims.put("id", user.getId());

            String accessToken = jwtService.generateAccessToken(user, claims);
            String refreshToken = jwtService.generateRefreshToken(user, claims);

            return new UserLoginResponseDTO(accessToken, refreshToken, null);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            //multi catch to if user exists or invalid credentials
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public RefreshTokenResponseDTO refreshAccessToken(String refreshToken) {
        Map<String, Object> claims = new HashMap<>();

        try {
            String username = jwtService.extractUsername(refreshToken, true);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(refreshToken, userDetails, true)) {
                return new RefreshTokenResponseDTO(null, "error validating the token");
            }

            MyUser user = userRepository.findByEmail(username).orElseThrow();
            claims.put("id", user.getId());
            String accessToken = jwtService.generateAccessToken(user, claims);

            return new RefreshTokenResponseDTO(accessToken, null);
        } catch (ResponseStatusException e) {
            //throwing the error got from jwtService class
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

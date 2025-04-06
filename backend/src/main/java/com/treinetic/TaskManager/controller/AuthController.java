package com.treinetic.TaskManager.controller;

import com.treinetic.TaskManager.DTO.request.UserLoginDTO;
import com.treinetic.TaskManager.DTO.request.UserRegisterDTO;
import com.treinetic.TaskManager.DTO.response.RefreshTokenResponseDTO;
import com.treinetic.TaskManager.DTO.response.UserLoginResponseDTO;
import com.treinetic.TaskManager.DTO.response.UserRegisterResponseDTO;
import com.treinetic.TaskManager.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authService;
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private int REFRESH_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 7;    //7d

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterDTO dto) {
        UserRegisterResponseDTO res = authService.register(dto);
        if (res.getError() != null && res.getEmail() == null) {
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (res.getError() != null) {
            //sending 409 to say email already exists
            return new ResponseEntity<>(res, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginDTO dto, HttpServletResponse response) {
        UserLoginResponseDTO res = authService.login(dto);
        if (res.getError() != null) {
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //sending refresh token in a cookie
        Cookie refreshTokenCookie = createRefreshTokenCookie(res.getRefreshToken());
        response.addCookie(refreshTokenCookie);

        return new ResponseEntity<>(new UserLoginResponseDTO(res.getAccessToken(), null, null), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false); //should be true for https
        response.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshAccessToken(
            @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            return new ResponseEntity<>(new RefreshTokenResponseDTO(null, "no cookie found"),
                    HttpStatus.UNAUTHORIZED);
        }

        RefreshTokenResponseDTO res = authService.refreshAccessToken(refreshToken);
        if (res.getError() != null) {
            return new ResponseEntity<>(new RefreshTokenResponseDTO(null, res.getError()),
                    HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new RefreshTokenResponseDTO(res.getAccessToken(), null), HttpStatus.OK);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(REFRESH_TOKEN_COOKIE_MAX_AGE);
        cookie.setPath("/"); //global path
        return cookie;
    }
}

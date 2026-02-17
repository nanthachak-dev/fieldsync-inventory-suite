package com.example.fieldsync_inventory_backend.controller.auth;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.auth.AuthenticateResponseDto;
import com.example.fieldsync_inventory_backend.dto.auth.AuthenticationRequestDto;
import com.example.fieldsync_inventory_backend.dto.auth.ChangePasswordRequestDto;
import com.example.fieldsync_inventory_backend.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // Disable this feature for prevent public registration
    // @PostMapping("/register")
    // public ResponseEntity<AuthenticateResponseDto> register(
    // @RequestBody RegisterRequestDto requestDto
    // ){
    // // Service
    // AuthenticateResponseDto responseDto = authService.register(requestDto);
    // return ResponseEntity.ok(responseDto);
    // }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponseDto> authenticate(
            @RequestBody AuthenticationRequestDto requestDto) {
        AuthenticateResponseDto responseDto = authService.authenticate(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequestDto request,
            Principal connectedUser) {
        authService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }
}

package com.example.fieldsync_inventory_api.service.auth;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.auth.AuthenticationRequestDto;
import com.example.fieldsync_inventory_api.dto.auth.RegisterRequestDto;
import com.example.fieldsync_inventory_api.dto.auth.AuthenticateResponseDto;
import com.example.fieldsync_inventory_api.dto.auth.ChangePasswordRequestDto;
import com.example.fieldsync_inventory_api.entity.auth.TokenEntity;
import com.example.fieldsync_inventory_api.entity.user.AppUserEntity;
import com.example.fieldsync_inventory_api.entity.user.RoleEntity;
import com.example.fieldsync_inventory_api.entity.user.UserRoleEntity;
import com.example.fieldsync_inventory_api.enums.TokenType;
import com.example.fieldsync_inventory_api.exception.BadRequestException;
import com.example.fieldsync_inventory_api.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_api.repository.user.AppUserRepository;
import com.example.fieldsync_inventory_api.repository.user.RoleRepository;
import com.example.fieldsync_inventory_api.repository.user.UserRoleRepository;
import com.example.fieldsync_inventory_api.repository.auth.TokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final AppUserRepository repository;
        private final RoleRepository roleRepository;
        private final UserRoleRepository userRoleRepository;
        private final TokenRepository tokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Transactional
        public AuthenticateResponseDto register(RegisterRequestDto requestDto) {
                Optional<AppUserEntity> existing = repository.findByUsername(requestDto.getUsername());
                if (existing.isPresent())
                        throw new BadRequestException("Can't register with existing user: "
                                        + requestDto.getUsername());

                AppUserEntity user = AppUserEntity.builder()
                                .username(requestDto.getUsername())
                                .password(passwordEncoder.encode(requestDto.getPassword()))
                                .build();

                AppUserEntity savedUser = repository.save(user);

                // Assign default role USER
                RoleEntity role = roleRepository.findByName("USER")
                                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

                UserRoleEntity userRole = UserRoleEntity.builder()
                                .user(savedUser)
                                .role(role)
                                .build();
                userRoleRepository.save(userRole);

                // Generate token
                String jwtToken = jwtService.generateToken(savedUser);
                saveUserToken(savedUser, jwtToken);

                return AuthenticateResponseDto.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticateResponseDto authenticate(AuthenticationRequestDto requestDto) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                requestDto.getUsername(),
                                                requestDto.getPassword()));
                // At this point the authentication is success
                AppUserEntity user = repository.findByUsername(requestDto.getUsername())
                                .orElseThrow(() -> new ResourceNotFoundException("User or password is wrong"));

                // Generate token
                String jwtToken = jwtService.generateToken(user);
                revokeAllUserTokens(user); // Revoke old token if exists
                saveUserToken(user, jwtToken);

                return AuthenticateResponseDto.builder()
                                .token(jwtToken)
                                .build();
        }

        @Transactional
        public void changePassword(ChangePasswordRequestDto request, Principal connectedUser) {
                var user = (AppUserEntity) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

                // check if the current password is correct
                if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                        throw new BadRequestException("Wrong password");
                }
                // check if the new password are the same
                if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                        throw new BadRequestException("Passwords do not match");
                }

                // update the password
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));

                // save the new password
                repository.save(user);
        }

        public void revokeTokensByUserId(Integer userId) {
                var user = repository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                revokeAllUserTokens(user);
        }

        private void revokeAllUserTokens(AppUserEntity user) {
                var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
                if (validUserTokens.isEmpty())
                        return;
                validUserTokens.forEach(token -> {
                        token.setExpired(true);
                        token.setRevoked(true);
                });
                tokenRepository.saveAll(validUserTokens);
        }

        private void saveUserToken(AppUserEntity user, String jwtToken) {
                var token = TokenEntity.builder()
                                .user(user)
                                .token(jwtToken)
                                .tokenType(TokenType.BEARER)
                                .expired(false)
                                .revoked(false)
                                .build();
                tokenRepository.save(token);
        }
}

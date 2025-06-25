package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.kafka.EventType;
import com.lfey.authservice.entity.Role;
import com.lfey.authservice.entity.RoleName;
import com.lfey.authservice.entity.UserRegistration;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.exception.*;
import com.lfey.authservice.repository.jpa.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
import com.lfey.authservice.service.security_service.TokenService;
import com.lfey.authservice.service.verification.GenerationCode;
import com.lfey.authservice.service.verification.VerificationCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final GenerationCode generationCode;
    private final TokenService tokenService;
    private final VerificationCode verificationCode;
    private final UserRepository userRepository;
    private final UserClientService userClientService;
    private final AuthenticationManager authenticationManager;

    public AuthService(PasswordEncoder passwordEncoder, GenerationCode generationCode, TokenService tokenService,
                       VerificationCode verificationCode, UserRepository userRepository, UserClientService userClientService,
                       AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.generationCode = generationCode;
        this.tokenService = tokenService;
        this.verificationCode = verificationCode;
        this.userRepository = userRepository;
        this.userClientService = userClientService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void  registerUser(UserRegistrationDto userRegistrationDTO) throws DuplicateUserException {
        var userRegistration = new UserRegistration(
                userRegistrationDTO.email(), userRegistrationDTO.username(),
                userRegistrationDTO.password(), userRegistrationDTO.phoneNumber(), null);

        if (!userRepository.existsByUsername(userRegistration.getUsername())) {
            if (userClientService.getUserByEmailFromUserService(userRegistration.getEmail()) == null) {
                userRegistration.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
                generationCode.generateCode(userRegistration, EventType.REGISTRATION);
            } else throw new DuplicateUserException(String.format("The user email: %s already exists", userRegistration.getEmail()));
        } else throw new DuplicateUserException(String.format("The user named: %s already exists", userRegistration.getUsername()));
    }

    //TODO Create method for validation reset password code

    @Transactional
    public JwtTokenDto getJWT(ValidationCodeDto validationCodeDto) throws UserCacheDataNotFoundException,
            InvalidCodeException {
        return tokenService.getToken(saveUserAfterRegistration(validationCodeDto).getUsername());
    }

    public UserAuthInfoDto getAuthInfoAndValidation(String token) throws InvalidJwtTokenException{
        return tokenService.getUserAuthInfo(token);
    }

    public JwtTokenDto refreshAccessToken(JwtTokenDto jwtTokenDto) {
        return tokenService.validationRefreshTokenAndGenerationAccessToken(jwtTokenDto.getRefreshToken());
    }

    @Transactional
    public Users saveUserAfterRegistration(ValidationCodeDto validationCodeDto) {
        UserRegistration userRegistration = verificationCode.verification(validationCodeDto);
        Users users = Users.builder()
                .username(userRegistration.getUsername())
                .password(userRegistration.getPassword())
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_USER).build());
        userRepository.save(users);
        userClientService.userRegistrationInUserService(UserDetailsDto.builder()
                .email(userRegistration.getEmail())
                .phoneNumber(userRegistration.getPhoneNumber())
                .username(userRegistration.getUsername())
                .build());
        return users;
    }

    public void logout(String refreshToken) {
        tokenService.deleteRefreshToken(refreshToken);
    }

    public JwtTokenDto login(AuthRequestDto authRequestDto) throws AuthenticationFailedException{
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDto.username(),
                            authRequestDto.password()
                    )
            );
            return tokenService.getToken(authentication.getName());
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }
}

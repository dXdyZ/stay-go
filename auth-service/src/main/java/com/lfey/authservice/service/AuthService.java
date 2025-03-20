package com.lfey.authservice.service;

import com.lfey.authservice.dto.*;
import com.lfey.authservice.dto.rabbit.EventType;
import com.lfey.authservice.entity.Role;
import com.lfey.authservice.entity.RoleName;
import com.lfey.authservice.entity.UserReg;
import com.lfey.authservice.entity.Users;
import com.lfey.authservice.exception.*;
import com.lfey.authservice.repository.jpa.UserRepository;
import com.lfey.authservice.service.clients.UserClientService;
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

    //TODO Добавить асинхронный вызов http запроса на проверку уникальности email пользователя
    @Transactional
    public void registerUser(UserReg userReg) throws DuplicateUserException {
        if (!userRepository.existsByUsername(userReg.getUsername())) {
            if (userClientService.getUserByEmailFromUserService(userReg.getEmail()) == null) {
                userReg.setPassword(passwordEncoder.encode(userReg.getPassword()));
                generationCode.generateCode(userReg, EventType.REGISTRATION);
            } else throw new DuplicateUserException(String.format("The user email: %s already exists", userReg.getEmail()));
        } else throw new DuplicateUserException(String.format("The user named: %s already exists", userReg.getUsername()));
    }

    //TODO Create method for validation reset password code

    @Transactional
    public JwtToken getJWT(ValidationCode validationCode) throws UserCacheDataNotFoundException, InvalidCodeException {
        return tokenService.getToken(saveUserAfterRegistration(validationCode).getUsername());
    }

    @Transactional
    public Users saveUserAfterRegistration(ValidationCode validationCode) {
        UserReg userReg = verificationCode.verification(validationCode);
        Users users = Users.builder()
                .username(userReg.getUsername())
                .password(userReg.getPassword())
                .build();
        users.getRoles().add(Role.builder().roleName(RoleName.ROLE_USER).build());
        userRepository.save(users);
        userClientService.userRegistrationInUserService(UserDto.builder()
                .email(userReg.getEmail())
                .phoneNumber(userReg.getPhoneNumber())
                .username(userReg.getUsername())
                .build());
        return users;
    }

    public void logout(String refreshToken) {
        tokenService.deleteRefreshToken(refreshToken);
    }

    public JwtToken login(AuthRequest authRequest) throws AuthenticationFailedException{
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(),
                            authRequest.password()
                    )
            );
            return tokenService.getToken(authentication.getName());
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }
}

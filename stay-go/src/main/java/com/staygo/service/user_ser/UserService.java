package com.staygo.service.user_ser;

import com.staygo.enity.DTO.UserDTO;
import com.staygo.enity.user.Payment;
import com.staygo.enity.user.Role;
import com.staygo.enity.user.Users;
import com.staygo.repository.user_repo.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final UserEmailExamination userEmailExamination;

    @Autowired
    public UserService(UserRepository userRepository, PaymentService paymentService, UserEmailExamination userEmailExamination) {
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.userEmailExamination = userEmailExamination;
    }

    public ResponseEntity<?> registerUser(Users users) {
        if (userRepository.findByUsername(users.getUsername()).isEmpty()) {
            users.setRole(Role.ROLE_USER);
            UserDTO userDTO = new UserDTO(users.getUsername(),
                    users.getPassword(), users.getEmail(),
                    users.getPhoneNumber(), users.getPayments());
            userEmailExamination.sendGenerationCodeOnUserEmail(users);
            return ResponseEntity.ok(userDTO);
        } return new ResponseEntity<>("Пользователь с таким именем уже существует", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> savedUser(Users users, Integer code) {
        if (userEmailExamination.examinationCode(users, code) != null) {
            userRepository.save(userEmailExamination.examinationCode(users, code));
            return ResponseEntity.ok("Вы успешно зарегестированны");
        } else return new ResponseEntity<>("Не привильный код", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> addedPaymentInformation(Payment payment, Principal principal) {
        Users user = userRepository.findByUsername(principal.getName()).get();
        payment.setUser(user);
        paymentService.addedPayment(payment);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> updateDataUser(@NotNull Users users) {
        Users user = userRepository.findByUsername(users.getUsername()).get();
        if (users.getUsername() != null) {
            user.setUsername(user.getUsername());
        }
        if (users.getEmail() != null) {
            user.setEmail(users.getEmail());
        }
        if (users.getPassword() != null) {
            user.setPassword(users.getPassword());
        }
        if (users.getPhoneNumber() != null) {
            user.setPhoneNumber(users.getPhoneNumber());
        }
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public Optional<Users> findByName(String username) {
        return userRepository.findByUsername(username);
    }


    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }


    public Optional<Users> findByPrincipal(@NotNull Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

    public Iterable<Users> findAll() {
        return userRepository.findAll();
    }
}

package com.lfey.authservice.service.verification;

import com.lfey.authservice.dto.UserReg;
import com.lfey.authservice.dto.ValidationCode;
import com.lfey.authservice.exception.InvalidCodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VerificationCodeTest {

    @Mock
    UserRegService userRegService;

    @InjectMocks
    VerificationCode verificationCode;


    @Test
    void verification_ValidCode() {
        var email = "test@test.com";
        var code = "123456";
        var validationCode = new ValidationCode(email, code);
        var userReg = UserReg.builder()
                .code(code)
                .email(email)
                .build();

        doReturn(userReg).when(this.userRegService).getUserRegByEmail(email);

        //when
        UserReg returnUserReg = this.verificationCode.verification(validationCode);

        //then
        verify(this.userRegService).getUserRegByEmail(email);
        assertEquals(code, userReg.getCode());
        assertEquals(email, userReg.getEmail());
    }

    @Test
    void verification_InvalidCode_ThrowInvalidCodeException() {
        //given
        var email = "test@test.com";
        var code = "123456";
        var validationCode = new ValidationCode(email, code);
        var userReg = UserReg.builder()
                .code("123345")
                .email(email)
                .build();

        doReturn(userReg).when(this.userRegService).getUserRegByEmail(email);

        //when
        InvalidCodeException exception = assertThrows(
                InvalidCodeException.class,
                () -> verificationCode.verification(validationCode)
        );

        //then
        assertEquals("Invalid code", exception.getMessage());
    }
}






package com.staygo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staygo.enity.user.Role;
import com.staygo.enity.user.Users;
import com.staygo.repository.user_repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
    }

    @Test
    public void userRegTest() throws Exception {
        Users users = usersTest(1L);
        mockMvc.perform(post("/user/register")
                .content(objectMapper.writeValueAsString(users))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").isString())
                .andExpect(jsonPath("$.password").isString())
                .andExpect(jsonPath("$.email").isString())
                .andExpect(jsonPath("$.phoneNumber").isString());

    }

    private Users usersTest(Long number) {
        Users users = new Users();
        return users.builder()
                .id(number)
                .username("user" + (Math.random() * 100))
                .password("user123123123" + number)
                .email("user@user.com")
                .createDate(new Date())
                .role(Role.ROLE_USER)
                .phoneNumber("+323132" + number)
                .build();
    }
}

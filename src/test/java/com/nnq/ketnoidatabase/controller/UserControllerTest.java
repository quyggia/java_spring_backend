package com.nnq.ketnoidatabase.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.service.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Userservice userservice;


    private UserCreationRequest request;
    private User user;
    private LocalDate dob;


    @BeforeEach
    void initData(){
        dob = LocalDate.of(2003,01,01);

        request = UserCreationRequest.builder()
                .username("nnq99")
                .firstname("NGUYEN")
                .lastname("NGOC")
                .password("12345678")
                .dob(dob)
                .build();

        user = user.builder()
                .id("s3j1h171h")
                .username("nnq99")
                .firstname("NGUYEN")
                .lastname("NGOC")
                .dob(dob)
                .build();


    }


    //Test them nguoi dung thanh cong
    @Test
    void createUser_validRequest_success() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();// khoi tao object de map request thanh kieu json(String)
        String content = objectMapper.writeValueAsString(request);

        //WHEN
        Mockito.when(userservice.createUser(ArgumentMatchers.any()))
                        .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("s3j1h171h")

        );//coi nhu mot request dc mo phong gui di
    }

    //Test ten dang nhap thieu ky tu
    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        //GIVEN
        request.setUsername("nn");
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();// khoi tao object de map request thanh kieu json(String)
        String content = objectMapper.writeValueAsString(request);

        //WHEN


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1004"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Lỗi đầu vào của tên đăng nhập phải đủ 4 ký tự.")

                );//coi nhu mot request dc mo phong gui di
    }
}

package com.nnq.ketnoidatabase.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.repository.UserRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    private UserResponse userResponse;
    private List<UserResponse> userResponseList;


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

        user = User.builder()
                .id("s3j1h171h")
                .username("nnq99")
                .firstname("NGUYEN")
                .lastname("NGOC")
                .dob(dob)
                .build();


        userResponse = UserResponse.builder()
                .id("s3j1h171h")
                .username("nnq99")
                .firstname("NGUYEN")
                .lastname("NGOC")
                .build();

        userResponseList = List.of(
                UserResponse.builder()
                        .id("1")
                        .username("user1")
                        .firstname("Nguyen")
                        .lastname("A")
                        .build(),
                UserResponse.builder()
                        .id("2")
                        .username("user2")
                        .firstname("Tran")
                        .lastname("B")
                        .build()
        );

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

    @Test
    void getMyUser_success() throws Exception {
        //GIVEN

        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();// khoi tao object de map request thanh kieu json(String)
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userservice.getMyUser()).thenReturn(userResponse);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/users/myinfo")
                    .with(user("nnq99"))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk()
        );
        //Then
    }

    @Test
    void getUser_success() throws Exception {
        //GIVEN
        Mockito.when(userservice.getUser()).thenReturn(userResponseList);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[1].username").value("user2")
        );
        //THEN
    }

    //Test get thong tin user thanh cong voi dieu kien get thong tin cua chinh no (Dung data)
    @Test
    void getUser_success_when_access_own_data_success() throws Exception {
        //GIVEN
        Mockito.when(userservice.getUser("s3j1h171h")).thenReturn(userResponse);
        //WHEN
        mockMvc.perform(
                    get("/users/{userId}", "s3j1h171h")
                            .with(jwt().jwt(jwt -> jwt.subject("nnq99"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("nnq99")
        );
        //THEN
    }



}

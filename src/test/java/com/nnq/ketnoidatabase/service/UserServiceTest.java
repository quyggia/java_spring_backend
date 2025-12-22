package com.nnq.ketnoidatabase.service;


import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.exception.AppException;
import com.nnq.ketnoidatabase.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import  static org.mockito.Mockito.when;
@SpringBootTest
public class UserServiceTest {


    @Autowired
    private Userservice userservice;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
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

        userResponse = UserResponse.builder()
                .id("s3j1h171h")
                .username("nnq99")
                .firstname("NGUYEN")
                .lastname("NGOC")
                .build();

        user = User.builder()
                .id("s3j1h171h")
                .username("nnq99")
                .firstname("NGUYEN")
                .lastname("NGOC")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        when(userRepository.existsUserByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var response = userservice.createUser(request);
        //THEN

        assertThat(response.getId()).isEqualTo("s3j1h171h");
        assertThat(response.getUsername()).isEqualTo("nnq99");

    }


    @Test
    void createUser_userExisted_fail() throws Exception {
        // GIVEN
        when(userRepository.existsUserByUsername(anyString())).thenReturn(true);

        //WHEN
        var exception = Assertions.assertThrows(AppException.class,
                ()-> userservice.createUser(request));
        //THEN
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);

    }

}

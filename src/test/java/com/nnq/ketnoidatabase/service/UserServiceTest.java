package com.nnq.ketnoidatabase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.exception.AppException;
import com.nnq.ketnoidatabase.mapper.UserMapper;
import com.nnq.ketnoidatabase.repository.UserRepository;

@SpringBootTest
// @TestPropertySource("/test.properties")
// @ExtendWith(MockitoExtension.class)
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @InjectMocks
    private Userservice userservice;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper; // <- BẮT BUỘC phải mock

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2003, 01, 01);

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

    //    @Test
    //    void createUser_validRequest_success() throws Exception {
    //        // GIVEN
    //        when(userRepository.existsUserByUsername(anyString())).thenReturn(false);
    //        when(userRepository.save(any())).thenReturn(user);
    //
    //        //WHEN
    //        var response = userservice.createUser(request);
    //        //THEN
    //
    //        assertThat(response.getId()).isEqualTo("s3j1h171h");
    //        assertThat(response.getUsername()).isEqualTo("nnq99");
    //
    //    }

    @Test
    void createUser_userExisted_fail() throws Exception {
        // GIVEN
        when(userRepository.existsUserByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = Assertions.assertThrows(AppException.class, () -> userservice.createUser(request));
        // THEN
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }
}

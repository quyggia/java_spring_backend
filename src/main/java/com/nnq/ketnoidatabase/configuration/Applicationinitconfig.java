package com.nnq.ketnoidatabase.configuration;


import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.enums.Role;
import com.nnq.ketnoidatabase.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class Applicationinitconfig {

    PasswordEncoder passwordEncoder;
    @Bean
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    ApplicationRunner applicationRunner(UserRepository userRepository)
    {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                var role = new HashSet<String>();
                role.add(Role.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        //.roles(role)
                        .build();
                userRepository.save(user);
                log.warn("Tài khoản admin của bạn đã được khởi tạo với mật khẩu là admin, hãy thay dổi nó.");
            }
            log.warn("Tài khoản admin của bạn đã được khởi tạo.");
        };
    }
}

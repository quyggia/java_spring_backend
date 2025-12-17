package com.nnq.ketnoidatabase.configuration;


import com.nnq.ketnoidatabase.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public final String[] PUBLIC_ENOPOINTS = {
            "/users", "/auth/login", "/auth/introspect"
    };
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //cấu hình phân quyền cho các requet
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENOPOINTS).permitAll()///chỉ ddịnh các đường dẫn post, get...
//                        .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())
                        //.hasAnyAuthority("ROLE_ADMIN") cach tu cau hinh
                        .anyRequest().authenticated());//còn lại phải được xác thực

        httpSecurity.oauth2ResourceServer(oauth2 ->//bảo vệ tài nguyên bằng cách xác thực token
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));//Khi request không nhận được token hoợp lệ hoawjc không có
        //token thì security sẽ gọi đến JwtAuthenticationEntryPoint để trả về lỗi cho clie
        httpSecurity.csrf(AbstractHttpConfigurer::disable);//vô hiệu hóa csrf
        return httpSecurity.build();//trả về kiểu SecurityFilterChain
    }
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter()//dunfg ddeer thay doi Prefix
    {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    JwtDecoder jwtDecoder() {

        SecretKeySpec scKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(scKeySpec)// nói với decode là cái token phải được ký bằng secret key này
                .macAlgorithm(MacAlgorithm.HS512)//nói với decode là token phải được ký ằng thuật toán này
                .build();
        //Nếu không sẽ nén ra lỗi exception

    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
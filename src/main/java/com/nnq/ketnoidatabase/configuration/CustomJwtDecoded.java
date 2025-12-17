package com.nnq.ketnoidatabase.configuration;


import com.nimbusds.jose.JOSEException;
import com.nnq.ketnoidatabase.dto.request.IntrospectRequest;
import com.nnq.ketnoidatabase.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoded implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;



    @Autowired
    private AuthenticationService authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            authenticationService.introspectResponse(IntrospectRequest.builder()
                            .token(token)
                    .build());

        } catch (JOSEException | ParseException e){
            throw  new  JwtException(e.getMessage());
        }

        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec scKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(scKeySpec)// nói với decode là cái token phải được ký bằng secret key này
                    .macAlgorithm(MacAlgorithm.HS512)//nói với decode là token phải được ký ằng thuật toán này
                    .build();
        }
        System.out.println(nimbusJwtDecoder);
        return nimbusJwtDecoder.decode(token);
    }
}

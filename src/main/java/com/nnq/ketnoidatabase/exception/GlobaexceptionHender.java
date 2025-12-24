package com.nnq.ketnoidatabase.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nnq.ketnoidatabase.dto.response.ApiRespon;
import com.nnq.ketnoidatabase.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobaexceptionHender {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiRespon<User>> henlingRuntimeexception(Exception exception) {

        ApiRespon<User> apiRespon = new ApiRespon<>();
        apiRespon.setCode(ErrorCode.LOI_CHUA_XACDINH.getCode());
        apiRespon.setMessage(ErrorCode.LOI_CHUA_XACDINH.getMessage());

        return ResponseEntity.status(ErrorCode.LOI_CHUA_XACDINH.getStatusCode()).body(apiRespon);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiRespon<Object>> henlingAppexception(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiRespon<Object> apiRespon = new ApiRespon<>();
        apiRespon.setCode(errorCode.getCode());
        apiRespon.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiRespon);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiRespon<Object>> hendlingDeniedexception(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UN_AUTHERIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiRespon.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiRespon<Object>> henlingValidation(MethodArgumentNotValidException exception) {
        String key = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.LOI_SAI_ERRORCODE;
        Map<String, Object> attributes = null;

        // là list json chứa các cặp key_value của annotation lưu trữ thành list vd min:18, plus:[]

        try {
            errorCode = ErrorCode.valueOf(key);
            // thử ánh xạ đến enum ErrorCode

            var constraintValidation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            // unwrap là ép cái đó thành ConstraintViolation để có thể lấy ra list key_value
            attributes = constraintValidation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
        } catch (IllegalArgumentException e) {
            // nếu thìm thấy sẽ trả veef giá trị tương ứng
            // còn không sẽ trả về null
        }

        ApiRespon<Object> apiRespon = new ApiRespon<>();
        apiRespon.setCode(errorCode.getCode());
        apiRespon.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiRespon);
    }

    private String mapAttribute(String message, Map<String, Object> attribute) {
        String messValue = String.valueOf(attribute.get(MIN_ATTRIBUTE));

        // lấy cái giá trị value của MIN_ATTRIBUTE trong attribute ép kiểu thành string  min : 18
        return message.replace("{" + MIN_ATTRIBUTE + "}", messValue);
        // Tháy thế MIN_ATTRIBUTE ở chuỗi bằng 18

    }
}

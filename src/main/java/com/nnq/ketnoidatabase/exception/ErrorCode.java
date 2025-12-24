package com.nnq.ketnoidatabase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    LOI_CHUA_XACDINH(9999, "Lỗi chưa xác đinh.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "Tên đăng nhập đã tồn tại.", HttpStatus.CONFLICT),
    USER_NOEXISTED(1002, "Tên đăng nhập không tồn tại.", HttpStatus.NOT_FOUND),
    LOI_SAI_ERRORCODE(1003, "Lỗi ErrCode bị sai.", HttpStatus.BAD_REQUEST),
    USERNAME_SIZE(1004, "Lỗi đầu vào của tên đăng nhập phải đủ 4 ký tự.", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE(1005, "Lỗi đầu vào của mật khẩu phải đủ 5 ký tự", HttpStatus.BAD_REQUEST),
    AUTHENTICATED(1006, "Đăng nhập không thành công, sai mật khẩu.", HttpStatus.UNAUTHORIZED),
    UN_AUTHERIZED(1007, "Bạn không có quyền truy cập.", HttpStatus.FORBIDDEN),
    INVALIDATE_DOB(1008, "Thông tin ngày tháng năm sinh truyền vào không đủ {min}.", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode() {}

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}

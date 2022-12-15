package com.vti.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageError {
    private String code;
    private String message;
    private Object param;

    public MessageError code(String code) {
        this.code = code;
        return this;
    }

    public MessageError message(String message) {
        this.message = message;
        return this;
    }

    public MessageError param(Object param) {
        this.param = param;
        return this;
    }
}

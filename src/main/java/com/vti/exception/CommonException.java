package com.vti.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonException extends RuntimeException implements ICommonException{
    private MessageError messageError;

    public CommonException messageError(MessageError messageError) {
        this.messageError = messageError;
        return this;
    }
}

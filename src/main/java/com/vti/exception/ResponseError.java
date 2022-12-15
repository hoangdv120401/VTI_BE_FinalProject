package com.vti.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseError {
    @JsonProperty("code")
    private String rpCode;
    @JsonProperty("status")
    private String rpStatus;
    @JsonProperty("message")
    private String rpMessage;

    private String detailMessage;

    public ResponseError rpCode(String rpCode) {
        this.rpCode = rpCode;
        return this;
    }

    public ResponseError rpStatus(String rpStatus) {
        this.rpStatus = rpStatus;
        return this;
    }

    public ResponseError rpMessage(String rpMessage) {
        this.rpMessage = rpMessage;
        return this;
    }

    public ResponseError detailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}

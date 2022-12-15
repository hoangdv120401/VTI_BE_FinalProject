package com.vti.utils;

import com.vti.exception.ICommonException;
import com.vti.exception.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Map;

@Component
public class HttpUtils {
    @Autowired
    private MessageSource messageSource;
    public MessageError populateMessage(ICommonException ex, Locale locale) {
        if (locale == null) {
            locale = new Locale("vi", "VI");
        }
        if (ex != null) {
            MessageError messageError = ex.getMessageError();

            String message = messageError.getMessage();
            String code = messageError.getCode();
            Object params = messageError.getParam();

            if (message == null || message.isEmpty()) {
                String defaultMessage = messageSource
                        .getMessage("defaultMessage", new Object[]{params},
                                "", locale);
                messageError.message(
                        messageSource
                                .getMessage(code, new Object[]{params},
                                        defaultMessage, locale));
            }
            return messageError;
        } else {
            try {
                throw new Exception();
            } catch (Exception exception) {
                System.out.println("error!!!");
            }
        }
        return new MessageError();
    }

    public String getLanguage(WebRequest webRequest) {
        return  webRequest.getHeader("lang") != null ? webRequest.getHeader("lang") : "en";
    };

    public String getLanguage(Map<String, String> headers) {
        return  headers.get("lang") != null ? headers.get("lang") : "vi";
    };
}

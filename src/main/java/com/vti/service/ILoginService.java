package com.vti.service;

import com.vti.dto.AccountDTO;
import com.vti.dto.LoginResponse;
import com.vti.form.LoginRequest;
import com.vti.form.RegisterRequest;

public interface ILoginService {
    AccountDTO register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}

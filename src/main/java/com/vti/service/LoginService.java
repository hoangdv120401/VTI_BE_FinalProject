package com.vti.service;

import com.vti.dto.AccountDTO;
import com.vti.dto.LoginResponse;
import com.vti.entity.Account;
import com.vti.entity.Enum.Role;
import com.vti.exception.CommonException;
import com.vti.exception.MessageError;
import com.vti.form.LoginRequest;
import com.vti.form.RegisterRequest;
import com.vti.repository.IAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class LoginService implements ILoginService{
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    @Transactional
    public AccountDTO register(RegisterRequest registerRequest) {
        validateRegister(registerRequest);
        Account account = new Account()
                .username(registerRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(Role.EMPLOYEE);
        accountRepository.save(account);
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    private void validateRegister(RegisterRequest registerRequest) {
        validateUserAndPasswordRegister(registerRequest.getUsername(), registerRequest.getPassword());
    }

    private void validateUserAndPasswordRegister(String username, String password) {
        validateUserNameRegister(username);
        validatePasswordRegister(password);
    }

    private void validatePasswordRegister(String password) {
        if (StringUtils.isEmpty(password)){
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("register.password.isNotValid"));
        }
    }

    private void validateUserNameRegister(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new CommonException()
                    .messageError(new MessageError().code("register.username.isNotValid"));
        }
        if (accountRepository.findAccountByUsername(username) != null){
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("register.username.isExisted"));
        }
        if(!username.matches("[a-zA-Z0-9\\\\._\\\\-]{3,}")){
            throw new CommonException()
                    .messageError(new MessageError().code("register.username.isNotValid"));
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        validateLogin(loginRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        Account account = accountRepository.findAccountByUsername(loginRequest.getUsername());
        LoginResponse loginResponse = modelMapper.map(account, LoginResponse.class);
        return loginResponse;
    }

    private void validateLogin(LoginRequest loginRequest) {
        validateUsernameLogin(loginRequest.getUsername());
        validatePasswordLogin(loginRequest.getPassword());
    }

    private void validatePasswordLogin(String password) {
        if (StringUtils.isEmpty(password)){
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("login.password.isNotValid"));
        }
    }

    private void validateUsernameLogin(String username) {
        if (StringUtils.isEmpty(username)){
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("login.username.isNotValid"));
        }
    }

}

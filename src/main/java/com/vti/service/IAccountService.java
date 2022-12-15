package com.vti.service;

import com.vti.dto.AccountDTO;
import com.vti.form.AccountCreatingForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdatingForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountService {
    Page<AccountDTO> getAllAccounts(Pageable pageable, AccountFilterForm form);

    AccountDTO getAccountByID(Integer id) ;

    AccountDTO createAccount(AccountCreatingForm form);

    AccountDTO updateAccount(Integer id, AccountUpdatingForm form);

    AccountDTO deleteAccount(Integer id);

    List<AccountDTO> deleteAllAccounts(List<Integer> ids);
}

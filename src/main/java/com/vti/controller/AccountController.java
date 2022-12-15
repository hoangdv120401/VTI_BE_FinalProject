package com.vti.controller;

import com.vti.dto.AccountDTO;
import com.vti.dto.DepartmentDTO;
import com.vti.form.AccountCreatingForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdatingForm;
import com.vti.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountController {
    @Autowired
    private IAccountService accountService;
    @GetMapping()
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(Pageable pageable, AccountFilterForm form){
        Page<AccountDTO> accountDTOPage = accountService.getAllAccounts(pageable, form);
        return ResponseEntity
                .ok()
                .body(accountDTOPage);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> getAccountByID(@PathVariable(name = "id") Integer id){
        AccountDTO accountDTO = accountService.getAccountByID(id);
        return ResponseEntity
                .ok()
                .body(accountDTO);
    }
    @PostMapping()
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountCreatingForm form){
        AccountDTO accountCreating = accountService.createAccount(form);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountCreating);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable(name = "id")Integer id,
            @RequestBody AccountUpdatingForm form){
        AccountDTO accountUpdating = accountService.updateAccount(id, form);
        return ResponseEntity
                .ok()
                .body(accountUpdating);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable(name = "id")Integer id){
        AccountDTO accountDTO = accountService.deleteAccount(id);
        return ResponseEntity
                .ok()
                .body(accountDTO);
    }
    @DeleteMapping()
    public ResponseEntity<List<AccountDTO>> deleteAllAccounts(@RequestParam List<Integer> ids){
        List<AccountDTO> accountDTOList = accountService.deleteAllAccounts(ids);
        return ResponseEntity
                .ok()
                .body(accountDTOList);
    }

}

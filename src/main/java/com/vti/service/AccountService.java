package com.vti.service;

import com.vti.common.Constant;
import com.vti.controller.AccountController;
import com.vti.dto.AccountDTO;
import com.vti.dto.DepartmentDTO;
import com.vti.entity.Account;
import com.vti.entity.Department;
import com.vti.entity.Enum.Role;
import com.vti.entity.Enum.Type;
import com.vti.exception.CommonException;
import com.vti.exception.MessageError;
import com.vti.form.AccountCreatingForm;
import com.vti.form.AccountFilterForm;
import com.vti.form.AccountUpdatingForm;
import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IDepartmentRepository departmentRepository;
    @Autowired
    private QueryService<Account> accountQueryService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<AccountDTO> getAllAccounts(Pageable pageable, AccountFilterForm form) {
        Specification<Account> where = buildWhere(form);
        Page<Account> accountPage = accountRepository.findAll(where, pageable);
        List<AccountDTO> accountDTOList = modelMapper.map(accountPage.getContent(), new TypeToken<List<AccountDTO>>() {
        }.getType());
        for (AccountDTO account : accountDTOList) {
            account.add(linkTo(methodOn(AccountController.class).getAccountByID(account.getId())).withSelfRel());
        }
        Page<AccountDTO> accountDTOPage = new PageImpl<>(accountDTOList, pageable, accountPage.getTotalElements());
        return accountDTOPage;
    }

    // xay dung filter
    private Specification<Account> buildWhere(AccountFilterForm form) {
        Specification<Account> where = Specification.where(null);
        if (form.getId() != null) {
            where = where.and(accountQueryService.buildIntegerFilter(Constant.ACCOUNT.ID, form.getId()));
        }
        if (form.getUsername() != null) {
            where = where.and(accountQueryService.buildStringFilter(Constant.ACCOUNT.USERNAME, form.getUsername()));
        }
        if (form.getSearch() != null) {
            where = where
                    .or(accountQueryService.buildStringFilter(Constant.ACCOUNT.USERNAME, form.getSearch()))
                    .or(accountQueryService.buildStringFilter(Constant.ACCOUNT.FIRST_NAME, form.getSearch()))
                    .or(accountQueryService.buildStringFilter(Constant.ACCOUNT.LAST_NAME, form.getSearch()));
        }
        return where;
    }

    @Override
    public AccountDTO getAccountByID(Integer id) {
        validateAccountID(id);
        Account account = accountRepository.findById(id).get();
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    private void validateAccountID(Integer id) {
        if (accountRepository.findById(id).isEmpty()) {
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("account.id.isNotExist"));
        }
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountCreatingForm form) {
        validateAccountCreating(form);
        Department department = departmentRepository.findById(form.getDepartmentId()).get();
        Account account = modelMapper.map(form, Account.class)
                .id(null)
                .password(bCryptPasswordEncoder.encode(form.getPassword()))
                .department(department);
        department.setTotalMember(department.getTotalMember() + 1);
        departmentRepository.save(department);
        accountRepository.save(account);
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    private void validateAccountCreating(AccountCreatingForm form) {
        validateUsername(form.getUsername());
        validateDepartment(form.getDepartmentId());
    }

    private void validateDepartment(Integer departmentId) {
        if (departmentRepository.findById(departmentId).isEmpty()){
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("account.create.department.isNotValid"));
        }

    }
    private void validateUsername(String username) {
        if (accountRepository.findAccountByUsername(username) != null) {
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("account.username.isExisted"));
        }
    }

    @Override
    @Transactional
    public AccountDTO updateAccount(
            Integer id,
            AccountUpdatingForm form) {
        validateAccountUpdating(id, form);
        form.setId(id);

        Account account = accountRepository.findById(id).get();
        // xu ly voi department cũ
        if (account.getDepartment() != null) {
            Department oldDept = account.getDepartment()
                    .totalMember(account.getDepartment().getTotalMember() - 1);
            departmentRepository.save(oldDept);
        }
        Department department = departmentRepository.findById(form.getDepartmentId()).get();
        department.totalMember(department.getTotalMember() + 1);
        departmentRepository.save(department);

        Account accountSave = modelMapper.map(form, Account.class)
                .password(bCryptPasswordEncoder.encode(form.getPassword()));
        accountRepository.save(accountSave);
        AccountDTO accountDTO = modelMapper.map(accountSave, AccountDTO.class);
        return accountDTO;
    }

    private void validateAccountUpdating(
            Integer id,
            AccountUpdatingForm form) {
        checkIDAccountIsValid(id);
        validateUsername(form.getUsername());
    }

    private void checkIDAccountIsValid(Integer id) {
        if (accountRepository.findById(id).isEmpty()) {
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("account.id.isNotValid"));
        }
    }

    @Override
    @Transactional
    public AccountDTO deleteAccount(Integer id) {
        validateAccountDeleting(id);
        Account account = accountRepository.findById(id).get();
        Department department = account.getDepartment();
        if (department != null) {
            department.totalMember(department.getTotalMember() - 1);
            departmentRepository.save(department);
        }
        accountRepository.delete(account);
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    @Override
    @Transactional
    public List<AccountDTO> deleteAllAccounts(List<Integer> ids) {
        validateAllAccounts(ids);
        List<Account> accountList = accountRepository.getAllAccounts(ids);
        List<AccountDTO> accountDTOList = modelMapper.map(accountList, new TypeToken<List<AccountDTO>>() {
        }.getType());
        accountRepository.deleteAllAccounts(ids);
        return accountDTOList;
    }

    private void validateAllAccounts(List<Integer> ids) {
        ids.forEach(id ->{
            checkIDAccountIsValid(id);
        });

    }

    private void validateAccountDeleting(Integer id) {
        checkIDAccountIsValid(id);
    }
}

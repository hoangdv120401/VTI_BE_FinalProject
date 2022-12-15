package com.vti.repository;

import com.vti.entity.Account;
import com.vti.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IAccountRepository extends
        JpaRepository<Account, Integer>,
        JpaSpecificationExecutor<Account> {
    Account findAccountByUsername(String username);
    // deleteAll
    @Modifying
    @Transactional
    @Query("DELETE FROM Account a WHERE a.id IN (:ids)")
    void deleteAllAccounts(@Param("ids") List<Integer> ids);
    // get all
    @Query("FROM Account a WHERE a.id IN (:ids)")
    List<Account> getAllAccounts(@Param("ids") List<Integer> ids);
}

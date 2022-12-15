package com.vti.form;

import com.vti.entity.Account;
import com.vti.entity.Enum.Type;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DepartmentCreatingForm {
    private String name;
    private Type type;
    private List<Account> accountList;
}

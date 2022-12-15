package com.vti.dto;

import com.vti.entity.Department;
import com.vti.entity.Enum.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class AccountDTO extends RepresentationModel<AccountDTO> {
    private Integer id;
    private String username;
    private String firstName;
    private String password;
    private String lastName;
    private String fullName;
    private Role role;
    private DepartmentDTO department;
}
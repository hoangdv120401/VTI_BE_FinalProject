package com.vti.entity;

import com.vti.entity.Enum.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "`Account`")
@Data
@NoArgsConstructor
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;
    @Column(name = "`password`", nullable = false, length = 800)
    private String password;
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;
    @Formula("concat(first_name,' ', last_name)")
    private String fullName;
    @Column(name = "`role`")
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    public Account id(Integer id) {
        this.id = id;
        return this;
    }

    public Account username(String username) {
        this.username = username;
        return this;
    }

    public Account password(String password) {
        this.password = password;
        return this;
    }

    public Account firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Account lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Account fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Account role(Role role) {
        this.role = role;
        return this;
    }

    public Account department(Department department) {
        this.department = department;
        return this;
    }
}

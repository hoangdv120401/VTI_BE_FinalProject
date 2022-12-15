package com.vti.entity;

import com.vti.entity.Enum.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`Department`")
@Data
@NoArgsConstructor
public class Department {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "`name`", length = 50, nullable = false, unique = true)
    private String name;
    @Column(name = "total_member")
    private Integer totalMember;
    @Column(name = "`type`", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;
    @OneToMany(mappedBy = "department")
    private List<Account> accountList;

    @PreUpdate
    public void changeDataBeforeUpdate() {
        if (createdDate == null) {
            createdDate = new Date();
        }
    }

    public Department id(Integer id) {
        this.id = id;
        return this;
    }

    public Department name(String name) {
        this.name = name;
        return this;
    }

    public Department totalMember(Integer totalMember) {
        this.totalMember = totalMember;
        return this;
    }

    public Department type(Type type) {
        this.type = type;
        return this;
    }

    public Department createdDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Department accountList(List<Account> accountList) {
        this.accountList = accountList;
        return this;
    }
}

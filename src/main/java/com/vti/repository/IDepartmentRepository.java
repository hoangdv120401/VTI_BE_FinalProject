package com.vti.repository;

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
public interface IDepartmentRepository extends
        JpaRepository<Department, Integer>,
        JpaSpecificationExecutor<Department> {
    Department findByName(String name);
    // deleteAll
    @Modifying
    @Transactional
    @Query("DELETE FROM Department d WHERE d.id IN (:ids)")
    void deleteAllDepartments(@Param("ids") List<Integer> ids);
    // get all
    @Query("FROM Department d WHERE d.id IN (:ids)")
    List<Department> getAllDepartments(@Param("ids") List<Integer> ids);
}

package com.vti.service;

import com.vti.dto.DepartmentDTO;
import com.vti.form.DepartmentCreatingForm;
import com.vti.form.DepartmentFilterForm;
import com.vti.form.DepartmentUpdatingForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDepartmentService {
    Page<DepartmentDTO> getAllDepartments(DepartmentFilterForm form, Pageable pageable);

    DepartmentDTO getDepartmentByID(Integer id);

    DepartmentDTO createDepartment(DepartmentCreatingForm form);

    DepartmentDTO updateDepartment(Integer id, DepartmentUpdatingForm form);

    DepartmentDTO deleteDepartment(Integer id);

    List<DepartmentDTO> deleteAllDepartments(List<Integer> ids);
}

package com.vti.service;

import com.vti.common.Constant;
import com.vti.controller.DepartmentController;
import com.vti.dto.DepartmentDTO;
import com.vti.entity.Account;
import com.vti.entity.Department;
import com.vti.entity.Enum.Type;
import com.vti.exception.CommonException;
import com.vti.exception.MessageError;
import com.vti.form.DepartmentCreatingForm;
import com.vti.form.DepartmentFilterForm;
import com.vti.form.DepartmentUpdatingForm;
import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private IDepartmentRepository departmentRepository;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private QueryService<Department> departmentQueryService;

    @Override
    public Page<DepartmentDTO> getAllDepartments(
            DepartmentFilterForm form,
            Pageable pageable) {
        Specification<Department> where = buildWhere(form);
        Page<Department> departmentPage = departmentRepository.findAll(where, pageable);
        List<DepartmentDTO> departmentDTOList = modelMapper.map(
                departmentPage.getContent(),
                new TypeToken<List<DepartmentDTO>>() {
                }.getType());
        for (DepartmentDTO department : departmentDTOList) {
            department.add(linkTo(methodOn(DepartmentController.class).getDepartmentByID(department.getId())).withSelfRel());
        }
        Page<DepartmentDTO> departmentDTOPage = new PageImpl<>(departmentDTOList, pageable, departmentPage.getTotalElements());
        return departmentDTOPage;
    }

    // xay dung filter department
    private Specification<Department> buildWhere(DepartmentFilterForm form) {
        Specification<Department> where = Specification.where(null);
        // search
        if (form.getSearch() != null) {
            where = where
                    .or(departmentQueryService.buildStringFilter(Constant.DEPARTMENT.NAME, form.getSearch()));
        }
        // filter
        if (form.getCreatedDate() != null) {
            where = where
                    .and(departmentQueryService.buildDateFilter(Constant.DEPARTMENT.CREATED_DATE, form.getCreatedDate()));
        }
        if (form.getType() != null) {
            where = where
                    .and(departmentQueryService.buildStringFilter(Constant.DEPARTMENT.TYPE, form.getType()));
        }
        return where;
    }

    // get one
    @Override
    public DepartmentDTO getDepartmentByID(Integer id) {
        validateIdDepartmentIsValid(id);
        Department department = departmentRepository.findById(id).get();
        DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);
        return departmentDTO;
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentCreatingForm form) {
        validateDepartmentCreating(form);
        Department department = modelMapper.map(form, Department.class);
        department.totalMember(form.getAccountList().size());
        departmentRepository.save(department);
        List<Account> accountList = form.getAccountList();
        for (Account account:
                accountList) {
            account.department(department);
        }
        accountRepository.saveAll(accountList);
        DepartmentDTO departmentCreated = modelMapper.map(department, DepartmentDTO.class);
        return departmentCreated;
    }

    // kiểm tra thông tin của department trước khi tạo
    private void validateDepartmentCreating(DepartmentCreatingForm form) {
        validateNameDepartmentIsExisted(form.getName());
        validateTypeDepartment(form.getType());
    }

    private void validateTypeDepartment(Type type) {
        if (!(Type.Dev.equals(type) ||
                Type.Test.equals(type) ||
                Type.ScrumMaster.equals(type) ||
                Type.PM.equals(type))) {
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("department.type.isNotValid"));
        }
    }

    private void validateNameDepartmentIsExisted(String name) {
        if (departmentRepository.findByName(name) != null) {
            throw new CommonException()
                    .messageError(new MessageError().code("department.name.isExisted"));
        }
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Integer id, DepartmentUpdatingForm form) {
        validateDepartmentUpdating(id, form);
        form.setId(id);
        Department department = modelMapper.map(form, Department.class);
        department.totalMember(form.getAccountList().size());
        departmentRepository.save(department);

        List<Account> accountList = form.getAccountList();
        for (Account account:
                accountList) {
            account.department(department);
        }
        accountRepository.saveAll(accountList);
        DepartmentDTO departmentUpdated = modelMapper.map(department, DepartmentDTO.class);
        return departmentUpdated;
    }

    private void validateDepartmentUpdating(Integer id, DepartmentUpdatingForm form) {
        validateIdDepartmentIsValid(id);
        validateNameDepartmentIsExisted(form.getName());
    }

    private void validateIdDepartmentIsValid(Integer id) {
        if (departmentRepository.findById(id).isEmpty()) {
            throw new CommonException()
                    .messageError(new MessageError()
                            .code("department.id.isNotValid"));
        }
    }

    @Override
    public DepartmentDTO deleteDepartment(Integer id) {
        validateDepartmentDeleting(id);
        Department department = departmentRepository.findById(id).get();
        DepartmentDTO departmentDeleted = modelMapper.map(department, DepartmentDTO.class);
        departmentRepository.deleteById(id);
        return departmentDeleted;
    }

    @Override
    @Transactional
    public List<DepartmentDTO> deleteAllDepartments(List<Integer> ids) {
        validateAllDepartment(ids);
        List<Department> departmentList = departmentRepository.getAllDepartments(ids);
        List<DepartmentDTO> departmentDTOList = modelMapper.map(departmentList, new TypeToken<List<DepartmentDTO>>() {
        }.getType());
        departmentRepository.deleteAllDepartments(ids);
        return departmentDTOList;
    }

    private void validateAllDepartment(List<Integer> ids) {
        ids.forEach(i -> {
            validateIdDepartmentIsValid(i);
        });
    }

    private void validateDepartmentDeleting(Integer id) {
        validateIdDepartmentIsValid(id);
    }
}

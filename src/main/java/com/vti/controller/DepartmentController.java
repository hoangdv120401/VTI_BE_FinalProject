package com.vti.controller;

import com.vti.dto.DepartmentDTO;
import com.vti.form.DepartmentCreatingForm;
import com.vti.form.DepartmentFilterForm;
import com.vti.form.DepartmentUpdatingForm;
import com.vti.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/departments")
@CrossOrigin("http://127.0.0.1:5500")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @GetMapping()
    public ResponseEntity<Page<DepartmentDTO>> getAllDepartments(
            DepartmentFilterForm form,
            Pageable pageable) {
        Page<DepartmentDTO> departmentDTO = departmentService.getAllDepartments(form, pageable);
        return ResponseEntity
                .ok()
                .body(departmentDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentByID(@PathVariable(name = "id") Integer id) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentByID(id);
        return ResponseEntity
                .ok()
                .body(departmentDTO);
    }
    @PostMapping()
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentCreatingForm form){
        DepartmentDTO departmentCreating = departmentService.createDepartment(form);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentCreating);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable(name = "id") Integer id,
            @RequestBody DepartmentUpdatingForm form){
        DepartmentDTO departmentUpdating = departmentService.updateDepartment(id, form);
        return ResponseEntity
                .ok()
                .body(departmentUpdating);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<DepartmentDTO> deleteDepartment(@PathVariable(name = "id") Integer id){
        DepartmentDTO departmentDeleting = departmentService.deleteDepartment(id);
        return ResponseEntity
                .ok()
                .body(departmentDeleting);
    }
    @DeleteMapping()
    public ResponseEntity<List<DepartmentDTO>> deleteAllDepartments(@RequestParam List<Integer> ids){
        List<DepartmentDTO> departmentDTOList = departmentService.deleteAllDepartments(ids);
        return ResponseEntity
                .ok()
                .body(departmentDTOList);
    }
}

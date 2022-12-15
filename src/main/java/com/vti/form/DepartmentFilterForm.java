package com.vti.form;

import com.vti.specification.filter.DateFilter;
import com.vti.specification.filter.StringFilter;
import lombok.Data;

@Data
public class DepartmentFilterForm {
    private StringFilter search;
    private DateFilter createdDate;
    private StringFilter type;
}

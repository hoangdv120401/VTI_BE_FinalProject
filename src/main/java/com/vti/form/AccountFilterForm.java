package com.vti.form;

import com.vti.specification.filter.IntegerFilter;
import com.vti.specification.filter.StringFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountFilterForm {
    private IntegerFilter id;
    private StringFilter username;
    private StringFilter search;

}

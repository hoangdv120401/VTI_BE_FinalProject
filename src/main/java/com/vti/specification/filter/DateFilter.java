package com.vti.specification.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class DateFilter extends Filter<Date> {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date greaterThan;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date greaterThanEqual;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date lessThan;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date lessThanEqual;
}

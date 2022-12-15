package com.vti.specification.filter;

import lombok.Data;

@Data
public class StringFilter  extends Filter<String>{
    String contains;
    String notContains;
}

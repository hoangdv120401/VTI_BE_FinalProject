package com.vti.specification.filter;

import lombok.Data;

@Data
public class IntegerFilter extends Filter<Integer> {
    Integer greaterThan;
    Integer greaterThanEqual;
    Integer lessThan;
    Integer lessThanEqual;
}

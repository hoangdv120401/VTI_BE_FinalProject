package com.vti.specification.filter;

import lombok.Data;

@Data
public class Filter<T> {
    T equals;
    T notEquals;
}

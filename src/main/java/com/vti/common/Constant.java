package com.vti.common;

public class Constant {
    public interface OPERATOR {
        String EQUALS = "equals";
        String NOT_EQUALS = "notEquals";
        String CONTAINS = "contains";
        String NOT_CONTAINS = "notContains";
        String GREATER_THAN = "greaterThan";
        String GREATER_THAN_EQUAL = "greaterThanEqual";
        String LESS_THAN = "lessThan";
        String LESS_THAN_EQUAL = "lessThanEqual";
    }

    public interface ACCOUNT {
        String ID = "id";
        String USERNAME = "username";
        String FIRST_NAME = "firstName";
        String LAST_NAME = "lastName";
        String DEPARTMENT_NAME ="departmentName";
    }

    public interface DEPARTMENT {
        String ID = "id";
        String NAME = "name";
        String TOTAL_MEMBER = "totalMember";
        String CREATED_DATE = "createdDate";
        String TYPE ="type";
    }
}

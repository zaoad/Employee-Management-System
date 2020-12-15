package com.example.employee_management.domain;

import java.util.Comparator;

public class sortByNameComparator implements Comparator<EmployeeInfo> {

    @Override
    public int compare(EmployeeInfo employeeInfo, EmployeeInfo t1) {
        return employeeInfo.getName().compareToIgnoreCase(t1.getName());
    }
}

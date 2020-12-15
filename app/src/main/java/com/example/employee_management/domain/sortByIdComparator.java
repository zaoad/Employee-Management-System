package com.example.employee_management.domain;

import java.util.Comparator;

public class sortByIdComparator implements Comparator<EmployeeInfo> {

    @Override
    public int compare(EmployeeInfo employeeInfo, EmployeeInfo t1) {
        return employeeInfo.get_id()-t1.get_id();
    }
}

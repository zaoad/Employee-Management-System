package com.example.employee_management.domain;


import java.util.Comparator;

public class EmployeeInfo {
    private int _id;
    private byte[] picture;
    private String name;
    private String age;
    private String gender;

    public EmployeeInfo(){}

    public EmployeeInfo(byte[] picture, String name, String age, String gender) {
        this.picture = picture;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public EmployeeInfo(int _id, byte[] picture, String name, String age, String gender) {
        this._id = _id;
        this.picture = picture;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public byte[] getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return "EmployeeInfo{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                "}\n";
    }

}

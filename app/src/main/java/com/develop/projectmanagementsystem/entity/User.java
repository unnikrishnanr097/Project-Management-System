package com.develop.projectmanagementsystem.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String department;
    private int role;

    public User(String name, String email, String department, int role) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", role=" + role +
                '}';
    }
}

package com.develop.projectmanagementsystem.entity;

import java.io.Serializable;
import java.util.Comparator;

public class User1 {
    private String name;
    private String email;
    private String department;
    private String role;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public User1() {

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static Comparator<User1> StatusComparator = new Comparator<User1>() {

        public int compare(User1 s1, User1 s2) {
            int status1
                    = s1.getStatus();
            int status2
                    = s2.getStatus();

            return status1- status2;
        }
    };
}

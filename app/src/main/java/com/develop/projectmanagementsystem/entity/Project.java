package com.develop.projectmanagementsystem.entity;

public class Project {

    private String assigned;
    private String projectName;
    private String sourceCodeLink;
    private String email;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSourceCodeLink() {
        return sourceCodeLink;
    }

    public void setSourceCodeLink(String sourceCodeLink) {
        this.sourceCodeLink = sourceCodeLink;
    }

    @Override
    public String toString() {
        return "Project{" +
                "assigned='" + assigned + '\'' +
                ", projectName='" + projectName + '\'' +
                ", sourceCodeLink='" + sourceCodeLink + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

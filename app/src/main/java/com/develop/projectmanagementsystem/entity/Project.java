package com.develop.projectmanagementsystem.entity;

public class Project {

    private String assigned;
    private String projectName;
    private String sourceCodeLink;
    private String email;
    private String status;
    private String headOfDepartment;
    private float hodRating;
    private float guideRating;
    private String hodReview;
    private String guideReview;


    public String getHodReview() {
        return hodReview;
    }

    public void setHodReview(String hodReview) {
        this.hodReview = hodReview;
    }

    public String getGuideReview() {
        return guideReview;
    }

    public void setGuideReview(String guideReview) {
        this.guideReview = guideReview;
    }

    public float getGuideRating() {
        return guideRating;
    }

    public void setGuideRating(float guideRating) {
        this.guideRating = guideRating;
    }

    public float getHodRating() {
        return hodRating;
    }

    public void setHodRating(float hodRating) {
        this.hodRating = hodRating;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

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

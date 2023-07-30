package com.example.readnreturn;

public class CourseModel2 {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private String course_name;
    private String author_name;
    private String status;
    private int imgid;

    public CourseModel2(String course_name, int imgid, String author_name, String status) {
        this.course_name = course_name;
        this.imgid = imgid;
        this.author_name=author_name;
        this.status = status;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

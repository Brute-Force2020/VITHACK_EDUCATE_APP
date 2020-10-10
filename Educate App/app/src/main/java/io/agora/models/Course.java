package io.agora.models;

import java.util.List;

public class Course {
    String courseId;
    String courseName;
    String topic;
    String tutorId;
    String coursePic;
    String courseDescription;
    String rating;
    List<String> registeredStudentsList;

    public Course() {
    }

    public Course(String courseId, String courseName, String topic, String tutorId, String coursePic, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.topic = topic;
        this.tutorId = tutorId;
        this.coursePic = coursePic;
        this.courseDescription = courseDescription;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getCoursePic() {
        return coursePic;
    }

    public void setCoursePic(String coursePic) {
        this.coursePic = coursePic;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getRegisteredStudentsList() {
        return registeredStudentsList;
    }

    public void setRegisteredStudentsList(List<String> registeredStudentsList) {
        this.registeredStudentsList = registeredStudentsList;
    }
}

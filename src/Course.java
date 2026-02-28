package Chap34;

public class Course {
    private final String courseId;
    private final String title;

    public Course(String courseId, String title) {
        this.courseId = courseId;
        this.title = title;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return courseId + "  -  " + title;
    }
}

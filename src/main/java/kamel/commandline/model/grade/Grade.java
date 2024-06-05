package kamel.commandline.model.grade;

public class Grade {
    private String studentName;
    private String courseName;
    private int grade;

    public Grade(String courseName, String studentName, int grade) {
        this.studentName = studentName;
        this.courseName = courseName;
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return courseName + "| " + studentName + ":" + grade;
    }
}

package kamel.commandline.service.instructor;

import kamel.commandline.model.course.Course;
import kamel.commandline.model.grade.Grade;
import kamel.commandline.model.grade.GradeDTO;

import java.util.List;

public interface InstructorService {
    List<Course> getCourses(int instructorId);
    List<Grade> getCourseGrades(int courseID);
    boolean editGrade(GradeDTO grade);
}

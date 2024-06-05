package kamel.commandline.data.course;

import kamel.commandline.model.course.Course;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public interface CoursesDao {
    Course findById(int id) throws NoSuchElementException, SQLException;
    List<Course> findByInstructorId(int id) throws NoSuchElementException, SQLException;
    List<Course> findAll() throws SQLException;
    boolean insertCourse(Course course) throws SQLException;
}

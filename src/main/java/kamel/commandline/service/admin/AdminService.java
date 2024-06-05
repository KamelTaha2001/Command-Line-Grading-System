package kamel.commandline.service.admin;

import kamel.commandline.model.course.Course;
import kamel.commandline.model.user.User;

import java.util.List;

public interface AdminService {
    List<User> getUsers();
    List<Course> getCourses();
    boolean createCourse(Course course);
    boolean createUser(User user);
    boolean addStudentToCourse(int studentId, int courseId);
}

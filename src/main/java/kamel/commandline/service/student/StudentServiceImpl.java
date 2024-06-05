package kamel.commandline.service.student;

import kamel.commandline.data.course.CoursesDao;
import kamel.commandline.data.course.JdbcCoursesDao;
import kamel.commandline.data.grade.GradesDao;
import kamel.commandline.data.grade.JdbcGradesDao;
import kamel.commandline.data.user.JdbcUsersDao;
import kamel.commandline.data.user.UsersDao;
import kamel.commandline.model.course.Course;
import kamel.commandline.model.grade.Grade;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {

    private final GradesDao gradesDao = new JdbcGradesDao();
    private final CoursesDao coursesDao = new JdbcCoursesDao();
    private final UsersDao usersDao = new JdbcUsersDao();

    @Override
    public List<Grade> getGrades(int studentId) {
        List<Grade> grades = new LinkedList<>();
        try {
            grades.addAll(
                    gradesDao.getGradesForStudent(studentId)
                    .stream()
                    .map(g -> {
                        try {
                            String courseName = coursesDao.findById(g.getCourseId()).getName();
                            String studentName = usersDao.findById(g.getStudentId()).getName();
                            return new Grade(courseName, studentName, g.getGrade());
                        } catch (Exception e) {
                            return new Grade("N/A", "N/A", 0);
                        }
                    }).toList());
        } catch (SQLException e) {
            grades.clear();
        }
        return grades;
    }

    @Override
    public double getAverage(List<Integer> grades) {
        OptionalDouble avg = grades.stream()
                .mapToInt(Integer::intValue)
                .average();
        if (avg.isEmpty()) return 0;
        return avg.getAsDouble();
    }
}

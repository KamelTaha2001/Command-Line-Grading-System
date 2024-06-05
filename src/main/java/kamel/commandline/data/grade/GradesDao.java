package kamel.commandline.data.grade;

import kamel.commandline.model.grade.GradeDTO;

import java.sql.SQLException;
import java.util.List;

public interface GradesDao {
    List<GradeDTO> getGradesForCourse(int courseId) throws SQLException;
    List<GradeDTO> getGradesForStudent(int studentId) throws SQLException;
    boolean insertGrade(GradeDTO grade) throws SQLException;
    boolean editGrade(GradeDTO grade) throws SQLException;
}

package kamel.commandline.service.student;

import kamel.commandline.model.grade.Grade;

import java.util.List;

public interface StudentService {
    List<Grade> getGrades(int studentId);
    double getAverage(List<Integer> grades);
}

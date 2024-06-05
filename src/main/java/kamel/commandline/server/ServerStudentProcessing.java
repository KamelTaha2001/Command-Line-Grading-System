package kamel.commandline.server;

import kamel.commandline.model.Choice;
import kamel.commandline.model.grade.Grade;
import kamel.commandline.model.user.User;
import kamel.commandline.service.student.StudentService;
import kamel.commandline.service.student.StudentServiceImpl;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ServerStudentProcessing implements ServerSideProcessingStrategy {

    private transient final StudentService studentService = new StudentServiceImpl();
    private transient PrintWriter out;

    @Override
    public void process(Socket client, User user) {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            out.println(getGrades(user.getId()));
            out.println(" ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            out.println(" ");
            out.println(e.getLocalizedMessage());
        }
    }

    @Override
    public List<Choice> setupChoices(BufferedReader in, PrintWriter out) {
        return new LinkedList<>();
    }

    private String getGrades(int id) throws SQLException {
        List<String> grades = new LinkedList<>();
        for (Grade grade : studentService.getGrades(id)) {
            grades.add(grade.toString());
        }
        return grades.toString();
    }
}

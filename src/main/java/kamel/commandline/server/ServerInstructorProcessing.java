package kamel.commandline.server;

import kamel.commandline.model.Choice;
import kamel.commandline.model.course.Course;
import kamel.commandline.model.grade.Grade;
import kamel.commandline.model.grade.GradeDTO;
import kamel.commandline.model.user.User;
import kamel.commandline.service.instructor.InstructorService;
import kamel.commandline.service.instructor.InstructorServiceImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ServerInstructorProcessing implements ServerSideProcessingStrategy {
    private User user;
    private transient final InstructorService instructorService = new InstructorServiceImpl();

    @Override
    public void process(Socket client, User user) {
        this.user = user;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            List<Choice> choices = setupChoices(in, out);
            int choice;
            do {
                out.println(choices);
                choice = Integer.parseInt(in.readLine());
                if (choice < 0 || choice >= choices.size())
                    continue;
                choices.get(choice).process();
            } while (choice != choices.size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Choice> setupChoices(BufferedReader in, PrintWriter out) {
        List<Choice> choices = new ArrayList<>();
        choices.add(new Choice(choices.size(), "Print courses", () -> {
            try {
                printCourses(out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Print grades for a course", () -> {
            try {
                printGrades(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Edit a grade", () -> {
            try {
                editGrade(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Exit", () -> {}));
        return choices;
    }

    private void printCourses(PrintWriter out) throws IOException {
        List<Course> courses = new LinkedList<>(instructorService.getCourses(user.getId()));
        out.println(courses);
        out.println(" ");
    }

    private void printGrades(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Enter course id: ");
        int courseId = Integer.parseInt(in.readLine());
        List<String> grades = new LinkedList<>();
        instructorService.getCourseGrades(courseId)
                .forEach(g -> {
                    grades.add(new Grade(g.getCourseName(), g.getStudentName(), g.getGrade()).toString());
                });
        out.println(grades);
        out.println(" ");
    }

    private void editGrade(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Enter course id: ");
        int courseId = Integer.parseInt(in.readLine());
        out.println("Enter student id: ");
        int studentId = Integer.parseInt(in.readLine());
        out.println("Enter grade: ");
        int grade = Integer.parseInt(in.readLine());
        boolean success = instructorService.editGrade(new GradeDTO(courseId, studentId, grade));
        if (success) out.println("Grade edited!");
        else out.println("Failed to edit grade!");
    }
}

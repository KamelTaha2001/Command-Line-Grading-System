package kamel.commandline.server;

import kamel.commandline.model.Choice;
import kamel.commandline.model.course.Course;
import kamel.commandline.model.user.User;
import kamel.commandline.model.user.UserRole;
import kamel.commandline.service.admin.AdminService;
import kamel.commandline.service.admin.AdminServiceImpl;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ServerAdminProcessing implements ServerSideProcessingStrategy {

    private transient final AdminService adminService = new AdminServiceImpl();

    @Override
    public void process(Socket client, User user) {
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
        choices.add(new Choice(choices.size(), "Create user", () -> {
            try {
                createUser(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Create course", () -> {
            try {
                createCourse(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Print users", () -> {
            try {
                printAllUsers(out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Print courses", () -> {
            try {
                printAllCourses(out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Add student to course", () -> {
            try {
                addStudentToCourse(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        choices.add(new Choice(choices.size(), "Exit", () -> {}));
        return choices;
    }

    private void createUser(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Enter user name: ");
        String name = in.readLine();
        out.println("Enter email: ");
        String email = in.readLine();
        out.println("Enter password: ");
        String password = in.readLine();
        out.println("Enter user role: ");
        UserRole role;
        try {
            role = UserRole.valueOf(in.readLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            role = UserRole.STUDENT;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(-1, email, name, hashedPassword, role);
        boolean success = adminService.createUser(user);
        if (success) out.println("User created!");
        else out.println("Failed to create user!");
    }

    private void createCourse(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Enter course name: ");
        String courseName = in.readLine();
        out.println("Enter instructor id: ");
        int instructorId = Integer.parseInt(in.readLine());
        boolean success = adminService.createCourse(new Course(-1, courseName,instructorId));
        if (success) out.println("Course created!");
        else out.println("Failed to create course!");
    }

    private void printAllUsers(PrintWriter out) throws IOException {
        out.println(adminService.getUsers());
        out.println(" ");
    }

    private void printAllCourses(PrintWriter out) throws IOException {
        List<String> courses = new LinkedList<>();
        for (Course c : adminService.getCourses()) {
            courses.add(c.toString());
        }
        out.println(courses);
        out.println(" ");
    }

    private void addStudentToCourse(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Enter student id: ");
        int studentId = Integer.parseInt(in.readLine());
        out.println("Enter course id: ");
        int courseId = Integer.parseInt(in.readLine());
        boolean success = adminService.addStudentToCourse(studentId, courseId);
        if (success) out.println("Student added!");
        else out.println("Failed to add student!");
    }
}

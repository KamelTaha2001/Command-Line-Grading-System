package kamel.commandline.client;

import kamel.commandline.model.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ClientAdminProcessing implements ClientSideProcessingStrategy {
    @Override
    public void process(Socket socket, User user) {
        try {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            boolean exit = false;
            while (!exit) {
                System.out.println("\n" + in.readLine());
                int choice = Integer.parseInt(userIn.readLine());
                out.println(choice);
                switch (choice) {
                    case 0 -> answerQuestions(4, userIn, in, out);
                    case 1, 4 -> answerQuestions(2, userIn, in, out);
                    case 2, 3 -> print(in);
                    case 5 -> exit = true;
                    default -> System.out.println("Wrong choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void answerQuestions(int numQuestions, BufferedReader userIn, BufferedReader in, PrintWriter out) throws IOException {
        for (int i = 0; i < numQuestions; i++) {
            System.out.println(in.readLine());
            out.println(userIn.readLine());
        }
        System.out.println(in.readLine());
    }

    private void print(BufferedReader in) throws IOException {
        Arrays.stream(in.readLine().replaceAll("[\\[\\]]", "").split(",")).forEach(s -> System.out.println(s.trim()));
        System.out.println(in.readLine());
    }
}
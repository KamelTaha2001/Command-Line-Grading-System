package kamel.commandline.client;

import kamel.commandline.model.user.User;
import kamel.commandline.server.GradingSystemServer;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class GradingSystemClient {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket(GradingSystemServer.IP, GradingSystemServer.PORT)
        ) {
            Objects.requireNonNull(login(socket)).processClient(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static User login(Socket socket) {
        try{
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            boolean loginSuccessful;
            do {
                System.out.println(in.readLine());
                String email = userInput.readLine();
                out.println(email);
                System.out.println(in.readLine());
                String password = userInput.readLine();
                out.println(password);
                loginSuccessful = Boolean.parseBoolean(in.readLine());
                if (!loginSuccessful)
                    System.out.println("Wrong credentials. Try again!");
            } while (!loginSuccessful);
            System.out.println("Login successful\n\n");
            return (User) objectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

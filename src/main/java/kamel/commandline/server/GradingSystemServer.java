package kamel.commandline.server;

import kamel.commandline.model.user.User;
import kamel.commandline.model.user.UserRole;
import kamel.commandline.service.LoginService;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NameNotFoundException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.AccessDeniedException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GradingSystemServer {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 11223;

    private static final LoginService loginService = new LoginService();

    public static void main(String[] args) {
        try(
                ServerSocket serverSocket = new ServerSocket(PORT);
                ExecutorService service = Executors.newCachedThreadPool()
        ) {
            System.out.println("Server is now running!");

            while (true) {
                Socket client = serverSocket.accept();
                service.execute(() -> {
                    try {
                        processLogin(client).processServer(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Couldn't establish connection to server!");
        }
    }

    private static User processLogin(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        ObjectOutputStream objectOut = new ObjectOutputStream(client.getOutputStream());

        User user = null;
        boolean success;
        do {
            out.println("Enter your email:");
            String email = in.readLine();
            out.println("Enter your password:");
            String password = in.readLine();
            try {
                user = loginService.authenticate(email, password);
                success = true;
            } catch (NameNotFoundException | AccessDeniedException e) {
                success = false;
            }
            out.println(success);
        } while (!success);
        objectOut.writeObject(user);
        objectOut.flush();
        return user;
    }
}

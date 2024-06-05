package kamel.commandline.client;

import kamel.commandline.model.user.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientStudentProcessing implements ClientSideProcessingStrategy {
    @Override
    public void process(Socket socket, User user) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String grades = in.readLine();
            System.out.println(grades);
            System.out.println(in.readLine());
        } catch (UnknownHostException e) {
            System.out.println("Couldn't find server!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't establish connection with server!");
        }
    }
}

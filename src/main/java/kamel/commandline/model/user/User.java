package kamel.commandline.model.user;

import kamel.commandline.client.ClientAdminProcessing;
import kamel.commandline.client.ClientInstructorProcessing;
import kamel.commandline.client.ClientSideProcessingStrategy;
import kamel.commandline.client.ClientStudentProcessing;
import kamel.commandline.server.ServerAdminProcessing;
import kamel.commandline.server.ServerInstructorProcessing;
import kamel.commandline.server.ServerSideProcessingStrategy;
import kamel.commandline.server.ServerStudentProcessing;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private UserRole role;
    private String password;
    private ServerSideProcessingStrategy serverProcessing;
    private ClientSideProcessingStrategy clientProcessing;

    public User(int id, String email, String name, String password, UserRole role) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.role = role;
        this.password = password;

        switch (role) {
            case STUDENT -> {
                serverProcessing = new ServerStudentProcessing();
                clientProcessing = new ClientStudentProcessing();
            }
            case ADMIN -> {
                serverProcessing = new ServerAdminProcessing();
                clientProcessing = new ClientAdminProcessing();
            }
            case INSTRUCTOR -> {
                serverProcessing = new ServerInstructorProcessing();
                clientProcessing = new ClientInstructorProcessing();
            }
        }
    }

    public void processServer(Socket client) {
        if (serverProcessing == null) throw new NullPointerException("User corrupted");
        serverProcessing.process(client, this);
    }

    public void processClient(Socket socket) {
        if (clientProcessing == null) throw new NullPointerException("User corrupted");
        clientProcessing.process(socket, this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public String getPassword() { return password; }

    @Override
    public String toString() {
        return id + ": " + name + " (" + role.name() + ")";
    }
}

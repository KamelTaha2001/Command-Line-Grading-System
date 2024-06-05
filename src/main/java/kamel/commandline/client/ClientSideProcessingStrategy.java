package kamel.commandline.client;

import kamel.commandline.model.user.User;

import java.io.Serializable;
import java.net.Socket;

public interface ClientSideProcessingStrategy extends Serializable {
    void process(Socket socket, User user);
}

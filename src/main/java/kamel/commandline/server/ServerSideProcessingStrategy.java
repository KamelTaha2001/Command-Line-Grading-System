package kamel.commandline.server;

import kamel.commandline.model.Choice;
import kamel.commandline.model.user.User;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;

public interface ServerSideProcessingStrategy extends Serializable {
    void process(Socket client, User user);
    List<Choice> setupChoices(BufferedReader in, PrintWriter out);
}

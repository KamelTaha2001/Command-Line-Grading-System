package kamel.commandline.service;

import kamel.commandline.data.user.JdbcUsersDao;
import kamel.commandline.data.user.UsersDao;
import kamel.commandline.model.user.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NameNotFoundException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

public class LoginService {

    private final UsersDao usersDao = new JdbcUsersDao();

    public User authenticate(String email, String password) throws NameNotFoundException, AccessDeniedException {
        try {
            User user = usersDao.findByEmail(email);
            boolean success = BCrypt.checkpw(password, user.getPassword());
            if (success)
                return user;
            throw new AccessDeniedException("Wrong credentials");
        } catch (SQLException e) {
            throw new NameNotFoundException();
        }
    }
}

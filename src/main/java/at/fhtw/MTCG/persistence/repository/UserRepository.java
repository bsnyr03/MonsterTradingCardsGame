package at.fhtw.MTCG.persistence.repository;
import at.fhtw.MTCG.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserRepository {
    User findByName(String name) throws SQLException;
    Collection<User> findAllUsers();
    boolean saveUser(User user) throws SQLException;
    void deleteUser(User user);
    void updateTocken(int userId, String token) throws SQLException;
}

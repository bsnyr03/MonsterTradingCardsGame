package at.fhtw.MTCG.persistence.repository;
import at.fhtw.MTCG.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserRepository {
    User findByName(String username) throws SQLException;
    Collection<User> findAllUsers() throws IllegalAccessException;
    boolean saveUser(User user) throws SQLException;
    void deleteUser(User user);
    void updateTocken(String username, String token) throws SQLException;
    void updateCoins(int id, int coins) throws SQLException;
}

package at.fhtw.MTCG.persistence.repository;
import at.fhtw.MTCG.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserRepository {
    User findByUsername(String username) throws SQLException;
    Collection<User> findAllUsers() throws IllegalAccessException;
    boolean saveUser(User user) throws SQLException;
    void deleteUser(User user);
    User findByToken(String token) throws SQLException;
    void updateTocken(String username, String token) throws SQLException;
    void updateCoins(int id, int coins) throws SQLException;
    int findUserIdByToken(String token) throws SQLException;
    boolean updateELOAndGamesPlayed(int userId, int eloChange) throws SQLException;
}

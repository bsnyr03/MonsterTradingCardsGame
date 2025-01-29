package at.fhtw.MTCG.persistence.repository;
import at.fhtw.MTCG.model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface UserRepository {
    User findByUsername(String username) throws SQLException;
    Collection<User> findAllUsers() throws IllegalAccessException;
    boolean saveUser(User user) throws SQLException;
    User findByToken(String token) throws SQLException;
    void updateTocken(String username, String token) throws SQLException;
    void updateCoins(int id, int coins) throws SQLException;
    int findUserIdByToken(String token) throws SQLException;
    boolean updateELOAndGamesPlayed(int userId, int eloChange) throws SQLException;
    void updateWinLossRecord(int userId, boolean won, boolean draw) throws SQLException;
    User getUserStatsByToken(String token) throws SQLException;
    List<User> getScoreboard() throws SQLException;
    void updateCoinsForExtraPrice(int userId, int coins) throws SQLException;
}

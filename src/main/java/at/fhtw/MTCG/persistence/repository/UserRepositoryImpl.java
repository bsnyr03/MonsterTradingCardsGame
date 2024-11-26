package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.persistence.UnitOfWork;
import at.fhtw.MTCG.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepositoryImpl implements UserRepository {
    private UnitOfWork unitOfWork;

    public UserRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public User findByName(String name) throws SQLException{
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setToken(resultSet.getString("token"));
                return user;
            }
        }
        return null;
    }

    @Override
    public Collection<User> findAllUsers() throws IllegalAccessException {
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                    SELECT * FROM users
                """)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<User> userRows = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
                userRows.add(user);
            }

            return userRows;
        } catch (SQLException e) {
            throw new IllegalAccessException("Select nicht erfolgreich");
        }
    }


    @Override
    public boolean saveUser(User user) throws SQLException{
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void updateTocken(String username, String token) throws SQLException{
        String sql = "UPDATE users SET token = ? WHERE username = ?";
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setString(1, token);
            statement.setString(2, username);
            statement.executeUpdate();
        }
    }
}

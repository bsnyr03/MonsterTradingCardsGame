package at.fhtw.MTCG.service;
import at.fhtw.MTCG.model.User;
import at.fhtw.MTCG.persistence.UnitOfWork;
import at.fhtw.MTCG.persistence.repository.UserRepository;
import at.fhtw.MTCG.persistence.repository.UserRepositoryImpl;

import java.sql.SQLException;
import java.util.Collection;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepositoryImpl(new UnitOfWork());
    }

    public boolean registerUser(User user) throws SQLException {
        if (userRepository.findByName(user.getUsername()) != null) {
            throw new IllegalStateException("User already exists");
        }
        return userRepository.saveUser(user);
    }

    public String loginUser(String username, String password) throws SQLException {
        User user = userRepository.findByName(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Überprüfen, ob bereits ein Token existiert
        if (user.getToken() == null || user.getToken().isEmpty()) {
            String token = "mtcgToken-" + username;
            userRepository.updateTocken(username, token);
            user.setToken(token); // Token speichern
        }

        return user.getToken(); // Bereits existierenden oder neuen Token zurückgeben
    }

    public boolean validateToken(String token) throws SQLException {
        User user = userRepository.findByToken(token);
        return user != null;
    }

    public Collection<User> findAllUsers() throws SQLException, IllegalAccessException {
        return userRepository.findAllUsers();
    }

    public User findUserByUsername(String username) throws SQLException {
        return userRepository.findByName(username);
    }
}
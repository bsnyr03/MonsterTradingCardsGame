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
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalStateException("User already exists");
        }
        return userRepository.saveUser(user);
    }

    public String loginUser(String username, String password) throws IllegalArgumentException, SQLException {
        User user = userRepository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Token wird nur beim ersten Login generiert oder wiederverwendet
        if (user.getToken() == null || user.getToken().isEmpty()) {
            String token = generateToken(username);
            user.setToken(token);
            userRepository.updateTocken(username, token);
        }

        return user.getToken();
    }

    private String generateToken(String username) {
        return "mtcgToken-" + username + "-" + System.currentTimeMillis();
    }

    public boolean validateToken(String token) throws SQLException {
        User user = userRepository.findByToken(token);
        return user != null;
    }

    public Collection<User> findAllUsers() throws SQLException, IllegalAccessException {
        return userRepository.findAllUsers();
    }

    public User findUserByUsername(String username) throws SQLException {
        return userRepository.findByUsername(username);
    }
}
package at.fhtw.MTCG.service;
import at.fhtw.MTCG.model.User;
import at.fhtw.MTCG.persistence.UnitOfWork;
import at.fhtw.MTCG.persistence.repository.UserRepository;
import at.fhtw.MTCG.persistence.repository.UserRepositoryImpl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

public class UserService {
    private UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepositoryImpl(new UnitOfWork());
    }

    public boolean registerUser(User user) throws SQLException {
        if(userRepository.findByName(user.getUsername())!= null){
            throw new IllegalStateException("User already exists");
        }
        return userRepository.saveUser(user);
    }

    public String loginUser(String username, String password) throws SQLException {
        User user = userRepository.findByName(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");

        }
        String token = "mtcgToken-" + UUID.randomUUID().toString();
        userRepository.updateTocken(username, token);
        return token;
    }

    public Collection<User> findAllUsers() throws SQLException, IllegalAccessException {
        return userRepository.findAllUsers();
    }

    public User findUserByUsername(String username) throws SQLException {
        return userRepository.findByName(username);
    }


}

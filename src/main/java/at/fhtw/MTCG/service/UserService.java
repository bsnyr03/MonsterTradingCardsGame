package at.fhtw.MTCG.service;
import at.fhtw.MTCG.model.User;
import at.fhtw.MTCG.persistence.repository.UserRepository;

import java.sql.SQLException;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(User user) throws SQLException {
        if(userRepository.findByName(user.getName())!= null){
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
        userRepository.updateTocken(user.getId(), token);
        return token;
    }



}

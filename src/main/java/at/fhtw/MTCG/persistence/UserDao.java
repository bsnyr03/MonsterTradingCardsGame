package at.fhtw.MTCG.persistence;

import at.fhtw.MTCG.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private List<User> UserData;

    public UserDao() {
        UserData = new ArrayList<>();
        UserData.add(new User("Baris","1234"));
        UserData.add(new User("Mike","1234"));
        UserData.add(new User("Kevin","1234"));
        UserData.add(new User("Christian","1234"));
    }

    //GET /users/:id
    public User getUser(Integer ID){
        User foundUser = UserData.stream()
                .filter(user -> ID == user.getId())
                .findAny()
                .orElse(null);
        return foundUser;
    }

    //GET /users

    public List<User> getUserData() {
        return UserData;
    }

    //POST /users
    public void addUserData(User user){
        UserData.add(user);
    }
}

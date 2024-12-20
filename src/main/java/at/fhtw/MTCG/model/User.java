package at.fhtw.MTCG.model;

import lombok.Getter;
import lombok.Setter;

public class User {
    private int id;
    private String username;
    private String password;
    private String token;
    private int coins = 20;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.token = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}

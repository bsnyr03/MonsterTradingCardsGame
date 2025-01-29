package at.fhtw.MTCG.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "testuser", 1000, 20, 5, 3, 1, 1);
    }

    @Test
    void testUserInitialization() {
        assertEquals(1, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals(1000, user.getElo());
        assertEquals(20, user.getCoins());
        assertEquals(5, user.getGamesPlayed());
        assertEquals(3, user.getGamesWon());
        assertEquals(1, user.getGamesDrawn());
        assertEquals(1, user.getGamesLost());
    }

    @Test
    void testUserTokenAssignment() {
        user.setToken("test-token");
        assertEquals("test-token", user.getToken());
    }

    @Test
    void testUpdateCoins() {
        user.setCoins(50);
        assertEquals(50, user.getCoins());
    }

    @Test
    void testUpdateElo() {
        user.setElo(1100);
        assertEquals(1100, user.getElo());
    }

    @Test
    void testUpdateGamesPlayed() {
        user.setGamesPlayed(10);
        assertEquals(10, user.getGamesPlayed());
    }

    @Test
    void testUpdateGamesWon() {
        user.setGamesWon(5);
        assertEquals(5, user.getGamesWon());
    }

    @Test
    void testUpdateGamesDrawn() {
        user.setGamesDrawn(2);
        assertEquals(2, user.getGamesDrawn());
    }

    @Test
    void testUpdateGamesLost() {
        user.setGamesLost(3);
        assertEquals(3, user.getGamesLost());
    }
}
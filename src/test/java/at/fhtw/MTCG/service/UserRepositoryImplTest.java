package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.User;
import at.fhtw.MTCG.persistence.UnitOfWork;
import at.fhtw.MTCG.persistence.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryImplTest {

    @Mock
    private UnitOfWork unitOfWork;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserRepositoryImpl(unitOfWork);

        when(unitOfWork.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void testFindByUsername() throws SQLException {
        // Arrange
        String username = "testuser";
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn(username);
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("token")).thenReturn("token123");

        // Act
        User user = userRepository.findByUsername(username);

        // Assert
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("token123", user.getToken());
        verify(preparedStatement, times(1)).setString(1, username);
    }

    @Test
    void testFindByUsernameNotFound() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Act
        User user = userRepository.findByUsername("nonexistent");

        // Assert
        assertNull(user);
        verify(preparedStatement, times(1)).setString(1, "nonexistent");
    }

    @Test
    void testFindAllUsers() throws SQLException, IllegalAccessException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false); // Simulate 2 rows in the result set
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("username")).thenReturn("user1", "user2");
        when(resultSet.getString("password")).thenReturn("password1", "password2");
        when(resultSet.getString("token")).thenReturn("token1", "token2");

        // Act
        Collection<User> users = userRepository.findAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    void testSaveUser() throws SQLException {
        // Arrange
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("newpassword");
        user.setToken("newtoken");

        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = userRepository.saveUser(user);

        // Assert
        assertTrue(result);
        verify(preparedStatement, times(1)).setString(1, "newuser");
        verify(preparedStatement, times(1)).setString(2, "newpassword");
        verify(preparedStatement, times(1)).setString(3, "newtoken");
        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testSaveUserRollbackOnError() throws SQLException {
        // Arrange
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("newpassword");
        user.setToken("newtoken");

        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(SQLException.class, () -> userRepository.saveUser(user));
        verify(unitOfWork, times(1)).rollbackTransaction();
    }

    @Test
    void testFindByToken() throws SQLException {
        // Arrange
        String token = "valid-token";
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn("testuser");
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("token")).thenReturn(token);

        // Act
        User user = userRepository.findByToken(token);

        // Assert
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(token, user.getToken());
        verify(preparedStatement, times(1)).setString(1, token);
    }

    @Test
    void testUpdateToken() throws SQLException {
        // Arrange
        String username = "testuser";
        String token = "new-token";
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        userRepository.updateTocken(username, token);

        // Assert
        verify(preparedStatement, times(1)).setString(1, token);
        verify(preparedStatement, times(1)).setString(2, username);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testUpdateCoins() throws SQLException {
        // Arrange
        int userId = 1;
        int coins = 50;
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        userRepository.updateCoins(userId, coins);

        // Assert
        verify(preparedStatement, times(1)).setInt(1, coins);
        verify(preparedStatement, times(1)).setInt(2, userId);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }
}
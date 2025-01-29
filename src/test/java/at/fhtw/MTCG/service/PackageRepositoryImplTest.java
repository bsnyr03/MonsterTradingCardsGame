package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.Package;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import at.fhtw.MTCG.persistence.UnitOfWork;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PackageRepositoryImplTest {
    private UnitOfWork unitOfWork;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private PackageRepositoryImpl packageRepository;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws SQLException {
        unitOfWork = mock(UnitOfWork.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        userRepository = mock(UserRepository.class);
        objectMapper = new ObjectMapper();

        when(unitOfWork.prepareStatement(anyString())).thenReturn(preparedStatement);
        packageRepository = new PackageRepositoryImpl(unitOfWork);
    }

    @Test
    void testGetCoinsByToken_Success() throws SQLException {
        String token = "test-token";
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("coins")).thenReturn(20);

        int coins = packageRepository.getCoinsByToken(token);
        assertEquals(20, coins);
    }

    @Test
    void testGetCoinsByToken_NoUser() throws SQLException {
        String token = "invalid-token";
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int coins = packageRepository.getCoinsByToken(token);
        assertEquals(0, coins);
    }

    @Test
    void testGetAvailablePackageId_Success() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);

        int packageId = packageRepository.getAvailablePackageId();
        assertEquals(1, packageId);
    }

    @Test
    void testGetAvailablePackageId_NoPackage() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int packageId = packageRepository.getAvailablePackageId();
        assertEquals(-1, packageId);
    }

    @Test
    void testGetCardsByPackageId_Success() throws SQLException, JsonProcessingException {
        int packageId = 1;
        List<Card> cards = Arrays.asList(
                new Card(1, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN),
                new Card(2, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER)
        );

        String cardsJson = objectMapper.writeValueAsString(cards);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("cards")).thenReturn(cardsJson);

        List<Card> retrievedCards = packageRepository.getCardsByPackageId(packageId);
        assertNotNull(retrievedCards);
        assertEquals(2, retrievedCards.size());
    }

    @Test
    void testGetCardsByPackageId_NoPackage() throws SQLException {
        int packageId = 1;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Card> retrievedCards = packageRepository.getCardsByPackageId(packageId);
        assertTrue(retrievedCards.isEmpty());
    }

    @Test
    void testUpdateCoins_Success() throws SQLException {
        String token = "test-token";
        int newCoins = 15;

        when(preparedStatement.executeUpdate()).thenReturn(1);

        packageRepository.updateCoins(token, newCoins);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testMarkPackageAsSold_Success() throws SQLException {
        int packageId = 1;
        int userId = 2;

        when(preparedStatement.executeUpdate()).thenReturn(1);

        packageRepository.markPackageAsSold(packageId, userId);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testCreatePackage_Success() throws SQLException, JsonProcessingException {
        String packageName = "Starter Package";
        List<Card> cards = Arrays.asList(
                new Card(1, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN),
                new Card(2, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER)
        );

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = packageRepository.createPackage(packageName, cards);
        assertTrue(result);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }
}
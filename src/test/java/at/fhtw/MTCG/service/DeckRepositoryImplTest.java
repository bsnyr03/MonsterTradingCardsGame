package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.Deck;
import at.fhtw.MTCG.persistence.UnitOfWork;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeckRepositoryImplTest {
    private UnitOfWork unitOfWork;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private DeckRepositoryImpl deckRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws SQLException {
        unitOfWork = mock(UnitOfWork.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        objectMapper = new ObjectMapper();

        when(unitOfWork.prepareStatement(anyString())).thenReturn(preparedStatement);
        deckRepository = new DeckRepositoryImpl(unitOfWork);
    }

    @Test
    void testCreateDeck_Success() throws SQLException, JsonProcessingException {
        int userId = 1;
        List<Card> cards = Arrays.asList(
                new Card(1, "FireGoblin", 50.0, at.fhtw.MTCG.model.enums.ElementTypeEnum.FIRE, at.fhtw.MTCG.model.enums.CardTypeEnum.MONSTER, at.fhtw.MTCG.model.enums.MonsterTypeEnum.GOBLIN),
                new Card(2, "WaterSpell", 40.0, at.fhtw.MTCG.model.enums.ElementTypeEnum.WATER, at.fhtw.MTCG.model.enums.CardTypeEnum.SPELL, at.fhtw.MTCG.model.enums.MonsterTypeEnum.NOMONSTER)
        );

        Deck deck = new Deck(userId, cards);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = deckRepository.createDeck(userId, deck);
        assertTrue(result);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testGetDeckByUserId_Success() throws SQLException, JsonProcessingException {
        int userId = 1;
        List<Card> cards = Arrays.asList(
                new Card(1, "FireGoblin", 50.0, at.fhtw.MTCG.model.enums.ElementTypeEnum.FIRE, at.fhtw.MTCG.model.enums.CardTypeEnum.MONSTER, at.fhtw.MTCG.model.enums.MonsterTypeEnum.GOBLIN),
                new Card(2, "WaterSpell", 40.0, at.fhtw.MTCG.model.enums.ElementTypeEnum.WATER, at.fhtw.MTCG.model.enums.CardTypeEnum.SPELL, at.fhtw.MTCG.model.enums.MonsterTypeEnum.NOMONSTER)
        );

        String cardsJson = objectMapper.writeValueAsString(cards);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("user_id")).thenReturn(userId);
        when(resultSet.getString("cards")).thenReturn(cardsJson);

        Deck retrievedDeck = deckRepository.getDeckByUserId(userId);
        assertNotNull(retrievedDeck);
        assertEquals(userId, retrievedDeck.getUserId());
        assertEquals(2, retrievedDeck.getCards().size());
    }

    @Test
    void testGetDeckByUserId_NotFound() throws SQLException {
        int userId = 1;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Deck retrievedDeck = deckRepository.getDeckByUserId(userId);
        assertNull(retrievedDeck);
    }

    @Test
    void testUpdateDeck_Success() throws SQLException, JsonProcessingException {
        int userId = 1;
        List<Card> cards = Arrays.asList(
                new Card(1, "FireGoblin", 50.0, at.fhtw.MTCG.model.enums.ElementTypeEnum.FIRE, at.fhtw.MTCG.model.enums.CardTypeEnum.MONSTER, at.fhtw.MTCG.model.enums.MonsterTypeEnum.GOBLIN),
                new Card(2, "WaterSpell", 40.0, at.fhtw.MTCG.model.enums.ElementTypeEnum.WATER, at.fhtw.MTCG.model.enums.CardTypeEnum.SPELL, at.fhtw.MTCG.model.enums.MonsterTypeEnum.NOMONSTER)
        );

        Deck deck = new Deck(userId, cards);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = deckRepository.updateDeck(userId, deck);
        assertTrue(result);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(unitOfWork, times(1)).commitTransaction();
    }
}
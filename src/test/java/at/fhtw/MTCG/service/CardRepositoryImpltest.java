package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import at.fhtw.MTCG.persistence.UnitOfWork;
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

class CardRepositoryImplTest {
    private UnitOfWork unitOfWork;
    private CardRepositoryImpl cardRepository;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        unitOfWork = mock(UnitOfWork.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(unitOfWork.prepareStatement(anyString())).thenReturn(preparedStatement);
        cardRepository = new CardRepositoryImpl(unitOfWork);
    }

    @Test
    void testFindById_CardExists() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("FireGoblin");
        when(resultSet.getDouble("damage")).thenReturn(50.0);
        when(resultSet.getString("element")).thenReturn("FIRE");
        when(resultSet.getString("type")).thenReturn("MONSTER");
        when(resultSet.getString("mtype")).thenReturn("GOBLIN");

        Card card = cardRepository.findById(1);

        assertNotNull(card);
        assertEquals(1, card.getId());
        assertEquals("FireGoblin", card.getName());
        assertEquals(50.0, card.getDamage());
        assertEquals(ElementTypeEnum.FIRE, card.getElement());
        assertEquals(CardTypeEnum.MONSTER, card.getType());
        assertEquals(MonsterTypeEnum.GOBLIN, card.getMonsterType());
    }

    @Test
    void testFindById_CardNotExists() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Card card = cardRepository.findById(1);

        assertNull(card);
    }

    @Test
    void testFindAll_CardsExist() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("FireGoblin", "WaterSpell");
        when(resultSet.getDouble("damage")).thenReturn(50.0, 40.0);
        when(resultSet.getString("element")).thenReturn("FIRE", "WATER");
        when(resultSet.getString("type")).thenReturn("MONSTER", "SPELL");
        when(resultSet.getString("mtype")).thenReturn("GOBLIN", "NOMONSTER");

        Collection<Card> cards = cardRepository.findAll();

        assertNotNull(cards);
        assertEquals(2, cards.size());
    }

    @Test
    void testSave_CardInsertedSuccessfully() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);

        Card card = new Card(0, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN);
        boolean isSaved = cardRepository.save(card);

        assertTrue(isSaved);
        assertEquals(1, card.getId());
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testDelete_CardExists() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean isDeleted = cardRepository.delete(1);

        assertTrue(isDeleted);
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testDelete_CardNotExists() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean isDeleted = cardRepository.delete(1);

        assertFalse(isDeleted);
    }

    @Test
    void testAssignCardToUser_Success() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean isAssigned = cardRepository.assignCardToUser(1, "testToken");

        assertTrue(isAssigned);
        verify(unitOfWork, times(1)).commitTransaction();
    }

    @Test
    void testFindCardsByToken() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("FireGoblin", "WaterSpell");
        when(resultSet.getDouble("damage")).thenReturn(50.0, 40.0);
        when(resultSet.getString("element")).thenReturn("FIRE", "WATER");
        when(resultSet.getString("type")).thenReturn("MONSTER", "SPELL");
        when(resultSet.getString("mtype")).thenReturn("GOBLIN", "NOMONSTER");

        Collection<Card> cards = cardRepository.findCardsByToken("testToken");

        assertNotNull(cards);
        assertEquals(2, cards.size());
    }

    @Test
    void testFindCardsByIds() throws SQLException {
        List<Integer> cardIds = Arrays.asList(1, 2);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("FireGoblin", "WaterSpell");
        when(resultSet.getDouble("damage")).thenReturn(50.0, 40.0);
        when(resultSet.getString("element")).thenReturn("FIRE", "WATER");
        when(resultSet.getString("type")).thenReturn("MONSTER", "SPELL");
        when(resultSet.getString("mtype")).thenReturn("GOBLIN", "NOMONSTER");

        List<Card> cards = cardRepository.findCardsByIds(cardIds, 1);

        assertNotNull(cards);
        assertEquals(2, cards.size());
    }

    @Test
    void testUpdateCardUser() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean isUpdated = cardRepository.updateCardUser(1, 2);

        assertTrue(isUpdated);
        verify(unitOfWork, times(1)).commitTransaction();
    }
}
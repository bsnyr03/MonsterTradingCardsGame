package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Deck;
import java.sql.SQLException;

public interface DeckRepository {
    boolean createDeck(int userId, Deck deck) throws SQLException;
    Deck getDeckByUserId(int userId) throws SQLException;
    boolean updateDeck(int userId, Deck deck) throws SQLException;
}
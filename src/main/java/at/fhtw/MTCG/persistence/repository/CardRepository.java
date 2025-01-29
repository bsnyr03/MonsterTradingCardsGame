package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface CardRepository {
    Card findById(int id) throws SQLException;
    Collection<Card> findAll() throws SQLException;
    boolean save(Card card) throws SQLException;
    boolean delete(int card) throws SQLException;
    boolean assignCardToUser(int cardId, String token) throws SQLException;
    Collection<Card> findCardsByToken(String token) throws SQLException;
    List<Card> findCardsByIds(List<Integer> cardIds, int userId) throws SQLException;
    boolean updateCardUser(int cardId, int newUserId) throws SQLException;
}

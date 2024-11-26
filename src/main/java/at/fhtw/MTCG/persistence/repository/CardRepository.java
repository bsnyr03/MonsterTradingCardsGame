package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;

import java.sql.SQLException;
import java.util.Collection;

public interface CardRepository {
    Card findById(int id) throws SQLException;
    Collection<Card> findAll() throws SQLException;
    boolean save(Card card) throws SQLException;
    boolean delete(Card card) throws SQLException;
}

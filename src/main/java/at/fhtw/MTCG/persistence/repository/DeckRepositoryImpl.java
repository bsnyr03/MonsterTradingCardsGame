package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.Deck;
import at.fhtw.MTCG.persistence.UnitOfWork;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DeckRepositoryImpl implements DeckRepository {
    private final UnitOfWork unitOfWork;

    public DeckRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public boolean createDeck(int userId, Deck deck) throws SQLException {
        String sql = "INSERT INTO decks (user_id, cards) VALUES (?, ?::jsonb)";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, userId);

            String cardsJson = new ObjectMapper().writeValueAsString(deck.getCards());
            statement.setString(2, cardsJson);

            return statement.executeUpdate() > 0;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting cards to JSON", e);
        }
    }

    @Override
    public Deck getDeckByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM decks WHERE user_id = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String cardsJson = resultSet.getString("cards");
                List<Card> cards = new ObjectMapper().readValue(cardsJson, List.class);

                return new Deck(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        cards
                );
            }

            return null; // Kein Deck gefunden
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reading cards from JSON", e);
        }
    }

    @Override
    public boolean updateDeck(int userId, Deck deck) throws SQLException {
        String sql = "UPDATE decks SET cards = ?::jsonb WHERE user_id = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            String cardsJson = new ObjectMapper().writeValueAsString(deck.getCards());
            statement.setString(1, cardsJson);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting cards to JSON", e);
        }
    }
}
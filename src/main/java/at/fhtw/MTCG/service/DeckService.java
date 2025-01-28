package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.Deck;
import at.fhtw.MTCG.persistence.repository.*;
import at.fhtw.MTCG.persistence.UnitOfWork;

import java.sql.SQLException;
import java.util.List;

public class DeckService {
    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public DeckService() {
        this.deckRepository = new DeckRepositoryImpl(new UnitOfWork());
        this.userRepository = new UserRepositoryImpl(new UnitOfWork());
        this.cardRepository = new CardRepositoryImpl(new UnitOfWork());
    }

    public boolean createDeck(int userId, Deck deck) throws SQLException {
        return deckRepository.createDeck(userId, deck);
    }

    public Deck getDeckByUserId(int userId) throws SQLException {
        Deck deck = deckRepository.getDeckByUserId(userId);
        if (deck == null) {
            throw new IllegalArgumentException("No deck found for user with ID: " + userId);
        }
        return deck;
    }

    public boolean updateDeck(int userId, Deck deck) throws SQLException {
        return deckRepository.updateDeck(userId, deck);
    }

    public int getUserIdFromToken(String token) throws SQLException {
        return userRepository.findUserIdByToken(token);
    }

    public List<Card> getCardsByIds(List<Integer> cardIds, int userId) throws SQLException {
        List<Card> cards = cardRepository.findCardsByIds(cardIds, userId);

        if (cards.size() != cardIds.size()) {
            throw new IllegalArgumentException("Some cards do not exist or do not belong to the user.");
        }
        return cards;
    }
}
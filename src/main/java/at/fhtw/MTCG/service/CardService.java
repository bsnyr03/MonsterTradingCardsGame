package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.User;
import at.fhtw.MTCG.persistence.repository.UserRepository;
import at.fhtw.MTCG.persistence.repository.CardRepository;
import at.fhtw.MTCG.persistence.repository.CardRepositoryImpl;
import at.fhtw.MTCG.persistence.UnitOfWork;
import at.fhtw.MTCG.persistence.repository.UserRepositoryImpl;

import java.sql.SQLException;
import java.util.Collection;

public class CardService {
    private final CardRepository cardRepository;

    public CardService() {
        this.cardRepository = new CardRepositoryImpl(new UnitOfWork());
    }

    public boolean createCard(Card card) throws SQLException {
        if (card.getName() == null || card.getName().isEmpty()) {
            throw new IllegalArgumentException("Card name cannot be null or empty");
        }
        return cardRepository.save(card);
    }

    public boolean deleteCard(int id) throws SQLException {
        return cardRepository.delete(id);
    }

    public boolean assignCardToUser(String cardId, String token) throws SQLException {
        return cardRepository.assignCardToUser(Integer.parseInt(cardId), token);
    }
    public Collection<Card> getCardsByToken(String token) throws SQLException {
        return cardRepository.findCardsByToken(token);
    }

}

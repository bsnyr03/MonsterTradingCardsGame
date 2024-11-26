package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.persistence.repository.CardRepository;
import at.fhtw.MTCG.persistence.repository.CardRepositoryImpl;
import at.fhtw.MTCG.persistence.UnitOfWork;

import java.sql.SQLException;
import java.util.Collection;

public class CardService {
    private final CardRepository cardRepository;

    public CardService() {
        this.cardRepository = new CardRepositoryImpl(new UnitOfWork());
    }

    // Abrufen aller Karten
    public Collection<Card> getAllCards() throws SQLException {
        return cardRepository.findAll();
    }

    // Abrufen einer Karte nach ID
    public Card getCardById(int id) throws SQLException {
        Card card = cardRepository.findById(id);
        if (card == null) {
            throw new IllegalArgumentException("Card not found with ID: " + id);
        }
        return card;
    }

    // Erstellen einer neuen Karte
    public boolean createCard(Card card) throws SQLException {
        if (card.getId() == 0) {
            throw new IllegalArgumentException("Card ID cannot be 0");
        }
        if (card.getName() == null || card.getName().isEmpty()) {
            throw new IllegalArgumentException("Card name cannot be null or empty");
        }
        return cardRepository.save(card);
    }

    // Löschen einer Karte nach ID
    public boolean deleteCard(int id) throws SQLException {
        return cardRepository.delete(id);
    }
}

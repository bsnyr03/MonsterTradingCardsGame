package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.persistence.repository.PackageRepository;
import at.fhtw.MTCG.persistence.repository.PackageRepositoryImpl;
import at.fhtw.MTCG.persistence.UnitOfWork;

import java.sql.SQLException;
import java.util.List;

public class PackageService {
    private final PackageRepository packageRepository;
    private final CardService cardService;

    public PackageService(CardService cardService) {
        this.packageRepository = new PackageRepositoryImpl(new UnitOfWork());
        this.cardService = cardService; // CardService hinzugefügt
    }

    public boolean createPackage(List<Card> cards) throws SQLException {
        // Validierung der Kartenanzahl
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A package must contain exactly 5 cards.");
        }

        // Speichern der Karten über CardService
        for (Card card : cards) {
            cardService.createCard(card); // Jede Karte wird individuell gespeichert
        }

        // Speichern des gesamten Packages
        return packageRepository.savePackage(cards);
    }
}
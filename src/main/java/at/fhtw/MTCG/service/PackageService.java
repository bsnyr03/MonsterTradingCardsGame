package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.persistence.repository.PackageRepository;
import at.fhtw.MTCG.persistence.repository.PackageRepositoryImpl;
import at.fhtw.MTCG.persistence.UnitOfWork;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class PackageService {
    private final PackageRepository packageRepository;
    private final CardService cardService;

    public PackageService() {
        this.packageRepository = new PackageRepositoryImpl(new UnitOfWork());
        this.cardService = new CardService();
    }

    public boolean createPackage(List<Card> cards) throws SQLException, JsonProcessingException {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A package must contain exactly 5 cards.");
        }

        for (Card card : cards) {
            cardService.createCard(card);
        }

        return packageRepository.createPackage("New Package", cards);
    }

    public List<Card> buyPackage(String token) throws SQLException {

        int coins = packageRepository.getCoinsByToken(token);
        if (coins < 5) {
            throw new IllegalArgumentException("Not enough coins to buy a package.");
        }

        int packageId = packageRepository.getAvailablePackageId();
        if (packageId == -1) {
            throw new IllegalStateException("No packages available for purchase.");
        }

        List<Card> cards = packageRepository.getCardsByPackageId(packageId);

        for (Card card : cards) {
            cardService.assignCardToUser(String.valueOf(card.getId()), token);
        }

        packageRepository.updateCoins(token, coins - 5);

        packageRepository.markPackageAsSold(packageId);

        return cards;
    }

    public Collection<Card> getPackagesByToken(String token) throws SQLException {
        return packageRepository.findPackagesByToken(token);
    }
}
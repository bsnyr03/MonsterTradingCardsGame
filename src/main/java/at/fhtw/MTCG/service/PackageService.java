package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.Package;
import at.fhtw.MTCG.persistence.repository.PackageRepository;
import at.fhtw.MTCG.persistence.repository.PackageRepositoryImpl;
import at.fhtw.MTCG.persistence.UnitOfWork;
import at.fhtw.MTCG.persistence.repository.UserRepository;
import at.fhtw.MTCG.persistence.repository.UserRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.*;

public class PackageService {
    private final PackageRepository packageRepository;
    private final CardService cardService;
    private final UserRepository userRepository;

    public PackageService() {
        this.packageRepository = new PackageRepositoryImpl(new UnitOfWork());
        this.cardService = new CardService();
        this.userRepository = new UserRepositoryImpl(new UnitOfWork());
    }

    public boolean createPackage(String packageName, List<Card> cards) throws SQLException, JsonProcessingException {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A package must contain exactly 5 cards.");
        }

        for (Card card : cards) {
            cardService.createCard(card);
        }

        return packageRepository.createPackage(packageName, cards);
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

        int userId = userRepository.findUserIdByToken(token);
        packageRepository.markPackageAsSold(packageId, userId);

        return cards;
    }

    public List<Map<String, Object>> getPackagesByToken(String token) throws SQLException {
        var packages = packageRepository.findPackagesByToken(token);

        List<Map<String, Object>> result = new ArrayList<>();
        for (var pkg : packages) {
            Map<String, Object> packageMap = new HashMap<>();
            packageMap.put("id", pkg.getId());
            packageMap.put("name", pkg.getName());
            packageMap.put("cards", pkg.getCards());
            result.add(packageMap);
        }
        return result;
    }
}
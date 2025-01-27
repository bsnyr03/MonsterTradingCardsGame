package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.Package;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.List;

public interface PackageRepository {
    int getCoinsByToken(String token) throws SQLException;
    int getAvailablePackageId() throws SQLException;
    List<Card> getCardsByPackageId(int packageId) throws SQLException;
    void updateCoins(String token, int newCoins) throws SQLException;
    void markPackageAsSold(int packageId) throws SQLException;
    boolean createPackage(String packageName, List<Card> cards) throws SQLException, JsonProcessingException;
    List<Package> findPackagesByToken(String token) throws SQLException;
    int findLatestPackageId() throws SQLException;
}
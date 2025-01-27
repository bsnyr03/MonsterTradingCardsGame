package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.persistence.UnitOfWork;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PackageRepositoryImpl implements PackageRepository {
    private final UnitOfWork unitOfWork;

    public PackageRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public int getCoinsByToken(String token) throws SQLException {
        String sql = "SELECT coins FROM users WHERE token = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("coins");
            }
        }
        return 0;
    }

    @Override
    public int getAvailablePackageId() throws SQLException {
        String sql = "SELECT id FROM packages WHERE sold = FALSE LIMIT 1";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        return -1;
    }

    @Override
    public List<Card> getCardsByPackageId(int packageId) throws SQLException {
        String sql = "SELECT cards FROM packages WHERE id = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, packageId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String cardsJson = resultSet.getString("cards");
                return new ObjectMapper().readValue(cardsJson, new TypeReference<List<Card>>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    @Override
    public void updateCoins(String token, int newCoins) throws SQLException {
        String sql = "UPDATE users SET coins = ? WHERE token = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, newCoins);
            statement.setString(2, token);
            statement.executeUpdate();
        }
    }

    @Override
    public void markPackageAsSold(int packageId) throws SQLException {
        String sql = "UPDATE packages SET sold = TRUE WHERE id = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, packageId);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean createPackage(String packageName, List<Card> cards) throws SQLException {
        String sql = "INSERT INTO packages (name, sold, cards) VALUES (?, FALSE, ?::jsonb)";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setString(1, packageName);
            String cardsJson = new ObjectMapper().writeValueAsString(cards);
            statement.setString(2, cardsJson);

            boolean result = statement.executeUpdate() > 0;
            unitOfWork.commitTransaction();
            return result;
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw e;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Card> findPackagesByToken(String token) throws SQLException {
        String sql = """
        SELECT p.cards
        FROM packages p
        JOIN users u ON u.token = ?
        WHERE p.sold = FALSE
    """;

        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();

            Collection<Card> allCards = new ArrayList<>();

            while (resultSet.next()) {
                String cardsJson = resultSet.getString("cards");
                List<Card> cards = new ObjectMapper().readValue(cardsJson, new TypeReference<List<Card>>() {});
                allCards.addAll(cards);
            }

            return allCards;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
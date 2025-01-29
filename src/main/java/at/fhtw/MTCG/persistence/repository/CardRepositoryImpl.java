package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import at.fhtw.MTCG.persistence.UnitOfWork;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {
    private final UnitOfWork unitOfWork;

    public CardRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public Card findById(int id) throws SQLException {
        String sql = "SELECT * FROM cards WHERE id = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Card(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("damage"),
                        ElementTypeEnum.valueOf(resultSet.getString("element")),
                        CardTypeEnum.valueOf(resultSet.getString("type")),
                        MonsterTypeEnum.valueOf(resultSet.getString("monsterType"))
                );
            }
        }
        return null;
    }

    @Override
    public Collection<Card> findAll() throws SQLException {
        String sql = "SELECT * FROM cards";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Collection<Card> cards = new ArrayList<>();

            while (resultSet.next()) {
                cards.add(new Card(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("damage"),
                        ElementTypeEnum.valueOf(resultSet.getString("element")),
                        CardTypeEnum.valueOf(resultSet.getString("type")),
                        MonsterTypeEnum.valueOf(resultSet.getString("monsterType"))
                ));
            }
            return cards;
        }
    }

    @Override
    public boolean save(Card card) throws SQLException {
        String sql = "INSERT INTO cards (name, damage, element, type, monster_type) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setString(1, card.getName());
            statement.setDouble(2, card.getDamage());
            statement.setString(3, card.getElement().toString());
            statement.setString(4, card.getType().toString());
            statement.setString(5, card.getMonsterType().toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                card.setId(resultSet.getInt("id"));
            }
            unitOfWork.commitTransaction();
            return true;
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw e;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM cards WHERE id = ?";
        try {
            try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
                statement.setInt(1, id);
                boolean result = statement.executeUpdate() > 0;
                unitOfWork.commitTransaction();
                return result;
            }
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw e;
        }
    }

    @Override
    public boolean assignCardToUser(int cardId, String token) throws SQLException {
        String sql = "UPDATE cards SET user_id = (SELECT id FROM users WHERE token = ?) WHERE id = ?";
        try{
            try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setString(1, token);
            statement.setInt(2, cardId);
            boolean result = statement.executeUpdate() > 0;
            unitOfWork.commitTransaction();
            return result;
            }
        } catch (SQLException e) {
        unitOfWork.rollbackTransaction();
        throw e;
        }
    }

    @Override
    public Collection<Card> findCardsByToken(String token) throws SQLException {
        String sql = """
        SELECT c.id, c.name, c.damage, c.element, c.type, c.monster_type
        FROM cards c
        JOIN users u ON c.user_id = u.id
        WHERE u.token = ?
    """;
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            Collection<Card> cards = new ArrayList<>();
            while (resultSet.next()) {
                cards.add(new Card(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("damage"),
                        ElementTypeEnum.valueOf(resultSet.getString("element")),
                        CardTypeEnum.valueOf(resultSet.getString("type")),
                        MonsterTypeEnum.valueOf(resultSet.getString("monster_type"))
                ));
            }
            return cards;
        }
    }

    @Override
    public List<Card> findCardsByIds(List<Integer> cardIds, int userId) throws SQLException {
        if (cardIds.isEmpty()) {
            throw new IllegalArgumentException("The card ID list is empty.");
        }

        String placeholders = String.join(",", cardIds.stream().map(id -> "?").toArray(String[]::new));
        String sql = "SELECT * FROM cards WHERE id IN (" + placeholders + ") AND user_id = ?";

        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            for (int i = 0; i < cardIds.size(); i++) {
                statement.setInt(i + 1, cardIds.get(i));
            }
            statement.setInt(cardIds.size() + 1, userId);

            ResultSet resultSet = statement.executeQuery();
            List<Card> cards = new ArrayList<>();
            while (resultSet.next()) {
                cards.add(new Card(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("damage"),
                        ElementTypeEnum.valueOf(resultSet.getString("element")),
                        CardTypeEnum.valueOf(resultSet.getString("type")),
                        MonsterTypeEnum.valueOf(resultSet.getString("monster_type"))
                ));
            }
            return cards;
        }
    }
    @Override
    public boolean updateCardUser(int cardId, int newUserId) throws SQLException {
        String sql = "UPDATE cards SET user_id = ? WHERE id = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, newUserId);
            statement.setInt(2, cardId);
            boolean result = statement.executeUpdate() > 0;
            unitOfWork.commitTransaction();
            return result;
        }
    }
}


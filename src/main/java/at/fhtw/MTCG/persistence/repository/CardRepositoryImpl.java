package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.persistence.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
                        CardTypeEnum.valueOf(resultSet.getString("type"))
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
                        CardTypeEnum.valueOf(resultSet.getString("type"))
                ));
            }
            return cards;
        }
    }

    @Override
    public boolean save(Card card) throws SQLException {
        String sql = "INSERT INTO cards (name, damage, element, type) VALUES (?, ?, ?, ?)";
        try {
            try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
                statement.setString(1, card.getName());
                statement.setDouble(2, card.getDamage());
                statement.setString(3, card.getElement().toString());
                statement.setString(4, card.getType().toString());
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
}


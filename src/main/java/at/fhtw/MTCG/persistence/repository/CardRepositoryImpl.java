package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.enums.CardType;
import at.fhtw.MTCG.model.enums.ElementType;
import at.fhtw.MTCG.persistence.UnitOfWork;

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
                        ElementType.valueOf(resultSet.getString("element")),
                        CardType.valueOf(resultSet.getString("type"))
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
                        ElementType.valueOf(resultSet.getString("element")),
                        CardType.valueOf(resultSet.getString("type"))
                ));
            }
            return cards;
        }
    }

    @Override
    public boolean save(Card card) throws SQLException {
        String sql = "INSERT INTO cards (id, name, damage, element, type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, card.getId());
            statement.setString(2, card.getName());
            statement.setDouble(3, card.getDamage());
            statement.setString(4, card.getElement().name());
            statement.setString(5, card.getType().name());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Card card) throws SQLException {
        String sql = "DELETE FROM cards WHERE id = ?";
        try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
            //statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }
}


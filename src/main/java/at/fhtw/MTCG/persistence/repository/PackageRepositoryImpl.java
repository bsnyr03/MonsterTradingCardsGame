package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.persistence.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PackageRepositoryImpl implements PackageRepository {
    private final UnitOfWork unitOfWork;

    public PackageRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public boolean savePackage(List<Card> cards) throws SQLException {
        String sql = "INSERT INTO cards (id, name, damage, element, type) VALUES (?, ?, ?, ?, ?)";
        for (Card card : cards) {
            try (PreparedStatement statement = unitOfWork.prepareStatement(sql)) {
                statement.setInt(1, card.getId());
                statement.setString(2, card.getName());
                statement.setDouble(3, card.getDamage());
                statement.setString(4, card.getElement().toString());
                statement.setString(5, card.getType().toString());
                statement.executeUpdate();
            }
        }
        return true;
    }
}
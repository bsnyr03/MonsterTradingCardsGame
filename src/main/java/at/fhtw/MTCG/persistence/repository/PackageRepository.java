package at.fhtw.MTCG.persistence.repository;

import at.fhtw.MTCG.model.Card;

import java.sql.SQLException;
import java.util.List;

public interface PackageRepository {
    boolean savePackage(List<Card> cards) throws SQLException;
}
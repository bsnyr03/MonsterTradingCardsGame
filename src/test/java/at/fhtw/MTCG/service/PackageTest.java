package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.persistence.repository.PackageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PackageServiceTest {

    @Test
    void testCreatePackageWithValidCards() throws SQLException {
        // Arrange
        CardService cardServiceMock = mock(CardService.class);
        PackageRepository packageRepositoryMock = mock(PackageRepository.class);

        PackageService packageService = new PackageService(cardServiceMock);

        List<Card> cards = Arrays.asList(
                new Card(100, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER),
                new Card(101, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL),
                new Card(102, "NormalKnight", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER),
                new Card(103, "FireDragon", 70.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER),
                new Card(104, "WaterElf", 25.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL)
        );

        when(packageRepositoryMock.savePackage(cards)).thenReturn(true);

        // Act
        boolean result = packageService.createPackage(cards);

        // Assert
        assertTrue(result);
        verify(cardServiceMock, times(5)).createCard(any(Card.class));
        verify(packageRepositoryMock, times(1)).savePackage(cards);
    }

    @Test
    void testCreatePackageWithInvalidCardCount() {
        // Arrange
        CardService cardServiceMock = mock(CardService.class);
        PackageService packageService = new PackageService(cardServiceMock);

        List<Card> invalidCards = Arrays.asList(
                new Card(1, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER)
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(invalidCards);
        });

        assertEquals("A package must contain exactly 5 cards.", exception.getMessage());
    }
}
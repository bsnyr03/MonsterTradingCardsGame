package at.fhtw.MTCG.model;

import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;
    private List<Card> cards;

    @BeforeEach
    void setUp() {
        cards = new ArrayList<>();
        cards.add(new Card(1, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN));
        cards.add(new Card(2, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER));
        cards.add(new Card(3, "NormalKnight", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.KNIGHT));
        cards.add(new Card(4, "FireDragon", 70.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.DRAGON));

        deck = new Deck(1, 100, cards);
    }

    @Test
    void testDeckCreation() {
        assertNotNull(deck);
        assertEquals(1, deck.getId());
        assertEquals(100, deck.getUserId());
        assertEquals(4, deck.getCards().size());
    }

    @Test
    void testGetCards() {
        List<Card> retrievedCards = deck.getCards();
        assertNotNull(retrievedCards);
        assertEquals(4, retrievedCards.size());
        assertEquals("FireGoblin", retrievedCards.get(0).getName());
    }

    @Test
    void testSetCards() {
        List<Card> newCards = new ArrayList<>();
        newCards.add(new Card(5, "WaterElf", 25.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER));

        deck.setCards(newCards);

        assertEquals(1, deck.getCards().size());
        assertEquals("WaterElf", deck.getCards().get(0).getName());
    }

    @Test
    void testDeckIsEmpty() {
        Deck emptyDeck = new Deck(2, 101, new ArrayList<>());
        assertTrue(emptyDeck.isEmpty(), "Deck should be empty.");
    }

    @Test
    void testDeckIsNotEmpty() {
        assertFalse(deck.isEmpty(), "Deck should not be empty.");
    }
}
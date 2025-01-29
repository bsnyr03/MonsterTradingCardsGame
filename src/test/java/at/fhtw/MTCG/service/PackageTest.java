package at.fhtw.MTCG.model;

import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackageTest {
    private Package cardPackage;
    private List<Card> cards;

    @BeforeEach
    void setUp() {
        cards = new ArrayList<>();
        cards.add(new Card(1, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN));
        cards.add(new Card(2, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER));
        cards.add(new Card(3, "NormalKnight", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.KNIGHT));
        cards.add(new Card(4, "FireDragon", 70.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.DRAGON));
        cards.add(new Card(5, "WaterElf", 25.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER));

        cardPackage = new Package(1, "Starter Package", cards);
    }

    @Test
    void testPackageInitialization() {
        assertEquals(1, cardPackage.getId());
        assertEquals("Starter Package", cardPackage.getName());
        assertEquals(5, cardPackage.getCards().size());
    }

    @Test
    void testSetPackageName() {
        cardPackage.setName("Advanced Package");
        assertEquals("Advanced Package", cardPackage.getName());
    }

    @Test
    void testSetPackageId() {
        cardPackage.setId(10);
        assertEquals(10, cardPackage.getId());
    }

    @Test
    void testSetAndGetCards() {
        List<Card> newCards = new ArrayList<>();
        newCards.add(new Card(6, "DarkOrk", 60.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.ORK));

        cardPackage.setCards(newCards);
        assertEquals(1, cardPackage.getCards().size());
        assertEquals("DarkOrk", cardPackage.getCards().get(0).getName());
    }
}
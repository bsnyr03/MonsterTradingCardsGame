package at.fhtw.MTCG.model;

import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testConstructorAndGetters() {
        Card card = new Card(1, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN);

        assertEquals(1, card.getId());
        assertEquals("FireGoblin", card.getName());
        assertEquals(50.0, card.getDamage());
        assertEquals(ElementTypeEnum.FIRE, card.getElement());
        assertEquals(CardTypeEnum.MONSTER, card.getType());
        assertEquals(MonsterTypeEnum.GOBLIN, card.getMonsterType());
    }



    @Test
    void testIsSpell() {
        Card spellCard = new Card(1, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER);
        Card monsterCard = new Card(2, "Goblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN);

        assertTrue(spellCard.isSpell());
        assertFalse(monsterCard.isSpell());
    }

    @Test
    void testIsEffectiveAgainst() {
        Card waterCard = new Card(3, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER);
        Card fireCard = new Card(4, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN);
        Card normalCard = new Card(5, "Knight", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.KNIGHT);

        assertTrue(waterCard.isEffectiveAgainst(fireCard));  // Wasser > Feuer
        assertTrue(fireCard.isEffectiveAgainst(normalCard)); // Feuer > Normal
        assertTrue(normalCard.isEffectiveAgainst(waterCard)); // Normal > Wasser

        assertFalse(fireCard.isEffectiveAgainst(waterCard)); // Feuer < Wasser
        assertFalse(waterCard.isEffectiveAgainst(normalCard)); // Wasser < Normal
        assertFalse(normalCard.isEffectiveAgainst(fireCard)); // Normal < Feuer
    }

    @Test
    void testIsIneffectiveAgainst() {
        Card waterCard = new Card(6, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER);
        Card fireCard = new Card(7, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN);
        Card normalCard = new Card(8, "Knight", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.KNIGHT);

        assertTrue(fireCard.isIneffectiveAgainst(waterCard)); // Feuer < Wasser
        assertTrue(waterCard.isIneffectiveAgainst(normalCard)); // Wasser < Normal
        assertTrue(normalCard.isIneffectiveAgainst(fireCard)); // Normal < Feuer

        assertFalse(waterCard.isIneffectiveAgainst(fireCard)); // Wasser > Feuer
        assertFalse(fireCard.isIneffectiveAgainst(normalCard)); // Feuer > Normal
        assertFalse(normalCard.isIneffectiveAgainst(waterCard)); // Normal > Wasser
    }
}
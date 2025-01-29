package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

    @Test
    void testEffectivenessCalculation() {
        Card waterSpell = new Card(1, "Water Spell", 30, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER);
        Card fireMonster = new Card(2, "Fire Monster", 40, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.DRAGON);

        assertEquals(60, waterSpell.calculateDamageAgainst(fireMonster), 0.1); // Effektiv: 30 * 2.0
        assertEquals(20, fireMonster.calculateDamageAgainst(waterSpell), 0.1); // Ineffektiv: 40 * 0.5
    }
}

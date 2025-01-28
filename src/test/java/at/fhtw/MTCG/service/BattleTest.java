package at.fhtw.MTCG.service;

import at.fhtw.MTCG.model.*;
import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    @Test
    void testMonsterVsMonster_NormalDamage() {
        User player1 = new User("Player1", new Deck(List.of(
                new Card(1, "Ork", 50.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        User player2 = new User("Player2", new Deck(List.of(
                new Card(2, "Knight", 40.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        Battle battle = new Battle(player1, player2);

        String result = battle.startBattle();

        assertTrue(result.contains("The winner is Player1"));
        assertTrue(battle.getBattleLog().contains("Player1 wins the round."));
    }

    @Test
    void testGoblinVsDragon() {
        User player1 = new User("Player1", new Deck(List.of(
                new Card(1, "Goblin", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        User player2 = new User("Player2", new Deck(List.of(
                new Card(2, "Dragon", 50.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        Battle battle = new Battle(player1, player2);

        String result = battle.startBattle();

        assertTrue(result.contains("The winner is Player2"));
        assertTrue(battle.getBattleLog().contains("Goblins are too afraid of Dragons to attack."));
    }

    @Test
    void testKrakenVsSpell() {
        User player1 = new User("Player1", new Deck(List.of(
                new Card(1, "Kraken", 70.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        User player2 = new User("Player2", new Deck(List.of(
                new Card(2, "WaterSpell", 60.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL)
        )).toString());
        Battle battle = new Battle(player1, player2);

        String result = battle.startBattle();

        assertTrue(result.contains("The winner is Player1"));
        assertTrue(battle.getBattleLog().contains("The Kraken is immune against spells."));
    }

    @Test
    void testWaterSpellDrownsKnight() {
        User player1 = new User("Player1", new Deck(List.of(
                new Card(1, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL)
        )).toString());
        User player2 = new User("Player2", new Deck(List.of(
                new Card(2, "Knight", 70.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        Battle battle = new Battle(player1, player2);

        String result = battle.startBattle();

        assertTrue(result.contains("The winner is Player1"));
        assertTrue(battle.getBattleLog().contains("The armor of Knights is so heavy that WaterSpells make them drown them instantly."));
    }

    @Test
    void testSpellEffectiveness() {
        User player1 = new User("Player1", new Deck(List.of(
                new Card(1, "FireSpell", 30.0, ElementTypeEnum.FIRE, CardTypeEnum.SPELL)
        )).toString());
        User player2 = new User("Player2", new Deck(List.of(
                new Card(2, "WaterMonster", 40.0, ElementTypeEnum.WATER, CardTypeEnum.MONSTER)
        )).toString());
        Battle battle = new Battle(player1, player2);

        String result = battle.startBattle();

        assertTrue(result.contains("The winner is Player2"));
        assertTrue(battle.getBattleLog().contains("damage is doubled"));
    }

    @Test
    void testBattleDrawAfter100Rounds() {
        User player1 = new User("Player1", new Deck(List.of(
                new Card(1, "Monster1", 50.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        User player2 = new User("Player2", new Deck(List.of(
                new Card(2, "Monster2", 50.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER)
        )).toString());
        Battle battle = new Battle(player1, player2);

        String result = battle.startBattle();

        assertTrue(result.contains("The battle ended in a draw after 100 rounds."));
    }
}
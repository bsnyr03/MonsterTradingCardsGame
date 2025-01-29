package at.fhtw.MTCG.model;

import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import at.fhtw.MTCG.persistence.repository.*;
import at.fhtw.MTCG.persistence.UnitOfWork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleTest {
    private DeckRepository deckRepository;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private UnitOfWork unitOfWork;
    private Battle battle;
    private Deck player1Deck;
    private Deck player2Deck;
    private List<Card> player1Cards;
    private List<Card> player2Cards;

    @BeforeEach
    void setUp() throws Exception {
        unitOfWork = mock(UnitOfWork.class);
        deckRepository = mock(DeckRepository.class);
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);

        player1Cards = new ArrayList<>();
        player2Cards = new ArrayList<>();

        // Beispielkarten für beide Spieler
        player1Cards.add(new Card(1, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN));
        player1Cards.add(new Card(2, "WaterSpell", 40.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER));
        player1Cards.add(new Card(3, "NormalKnight", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.KNIGHT));
        player1Cards.add(new Card(4, "FireDragon", 70.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.DRAGON));

        player2Cards.add(new Card(5, "WaterElf", 25.0, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER));
        player2Cards.add(new Card(6, "FireGoblin", 50.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN));
        player2Cards.add(new Card(7, "NormalKnight", 30.0, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.KNIGHT));
        player2Cards.add(new Card(8, "FireDragon", 70.0, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.DRAGON));
        player1Deck = new Deck(1, player1Cards);
        player2Deck = new Deck(2, player2Cards);

        when(deckRepository.getDeckByUserId(1)).thenReturn(player1Deck);
        when(deckRepository.getDeckByUserId(2)).thenReturn(player2Deck);

        battle = new Battle(1, 2);
    }

    @Test
    void testStartBattle() throws SQLException {
        String result = battle.startBattle();
        assertNotNull(result);
        assertTrue(result.contains("The battle is over"));
    }


    @Test
    void testFightGoblinVsDragon() {
        Card goblin = new Card(1, "Goblin", 50, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.GOBLIN);
        Card dragon = new Card(2, "Dragon", 100, ElementTypeEnum.FIRE,CardTypeEnum.MONSTER,MonsterTypeEnum.DRAGON);

        int result = battle.fight(goblin, dragon);
        assertEquals(-1, result, "Goblin should always lose to Dragon.");
    }

    @Test
    void testFightWizardVsOrk() {
        Card wizard = new Card(3, "Wizard", 60, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.WIZARD);
        Card ork = new Card(4, "Ork", 80, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.ORK);

        int result = battle.fight(wizard, ork);
        assertEquals(1, result, "Wizard should always control Ork.");
    }

    @Test
    void testFightKrakenVsSpell() {
        Card kraken = new Card(5, "Kraken", 80, ElementTypeEnum.WATER,CardTypeEnum.MONSTER, MonsterTypeEnum.KRAKEN);
        Card spell = new Card(6, "WaterSpell", 50, ElementTypeEnum.WATER, CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER);

        int result = battle.fight(kraken, spell);
        assertEquals(1, result, "Kraken should always be immune to spells.");
    }

    @Test
    void testWaterSpellDrownsKnight() {
        Card knight = new Card(7, "Knight", 70, ElementTypeEnum.NORMAL, CardTypeEnum.MONSTER, MonsterTypeEnum.KNIGHT);
        Card waterSpell = new Card(8, "WaterSpell", 50, ElementTypeEnum.WATER,CardTypeEnum.SPELL, MonsterTypeEnum.NOMONSTER);

        int result = battle.fight(knight, waterSpell);
        assertEquals(-1, result, "WaterSpell should instantly drown Knights.");
    }

    @Test
    void testFireElfEvadesDragon() {
        Card fireElf = new Card(9, "FireElf", 40, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.FIREELF);
        Card dragon = new Card(10, "Dragon", 100, ElementTypeEnum.FIRE, CardTypeEnum.MONSTER, MonsterTypeEnum.DRAGON);

        int result = battle.fight(fireElf, dragon);
        assertEquals(1, result, "FireElf should always evade Dragon's attack.");
    }



    @Test
    void testConcludeBattle_Player1Wins() throws SQLException {
        player2Deck.getCards().clear(); // Spieler 2 verliert alle Karten
        String result = battle.concludeBattle(10);

        assertTrue(result.contains("Player 1"), "Player 1 should be declared the winner.");
    }


}
package at.fhtw.MTCG.model;

import at.fhtw.MTCG.persistence.repository.DeckRepository;
import at.fhtw.MTCG.persistence.repository.DeckRepositoryImpl;
import at.fhtw.MTCG.persistence.UnitOfWork;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private final Deck player1Deck;
    private final Deck player2Deck;
    private final List<String> battleLog;
    private static final int MAX_ROUNDS = 100;

    public Battle(int player1Id, int player2Id) throws Exception {
        DeckRepository deckRepository = new DeckRepositoryImpl(new UnitOfWork());

        this.player1Deck = deckRepository.getDeckByUserId(player1Id);
        this.player2Deck = deckRepository.getDeckByUserId(player2Id);

        if (player1Deck == null || player2Deck == null) {
            throw new IllegalArgumentException("One or both players do not have a valid deck.");
        }

        this.battleLog = new ArrayList<>();
    }

    public String startBattle() {
        int roundCount = 0;

        while (!player1Deck.getCards().isEmpty() && !player2Deck.getCards().isEmpty() && roundCount < MAX_ROUNDS) {
            roundCount++;
            battleLog.add("Round " + roundCount + ":");

            Card card1 = drawRandomCard(player1Deck);
            Card card2 = drawRandomCard(player2Deck);

            battleLog.add("Player 1 plays: " + card1.getName());
            battleLog.add("Player 2 plays: " + card2.getName());

            int result = fight(card1, card2);

            if (result > 0) {
                // Player 1 wins the round
                player1Deck.getCards().add(card2);
                player2Deck.getCards().remove(card2);
                battleLog.add("Player 1 wins this round.");
            } else if (result < 0) {
                // Player 2 wins the round
                player2Deck.getCards().add(card1);
                player1Deck.getCards().remove(card1);
                battleLog.add("Player 2 wins this round.");
            } else {
                battleLog.add("This round is a draw.");
            }
        }

        return concludeBattle(roundCount);
    }

    private Card drawRandomCard(Deck deck) {
        List<Card> cards = deck.getCards();
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty.");
        }
        return cards.get((int) (Math.random() * cards.size()));
    }

    private int fight(Card card1, Card card2) {
        // Spezialfälle
        if ("GOBLIN".equals(card1.getType()) && "DRAGON".equals(card2.getType())) {
            return -1;
        }
        if ("GOBLIN".equals(card2.getType()) && "DRAGON".equals(card1.getType())) {
            return 1;
        }
        if ("WIZARD".equals(card1.getType()) && "ORK".equals(card2.getType())) {
            return 1;
        }
        if ("WIZARD".equals(card2.getType()) && "ORK".equals(card1.getType())) {
            return -1;
        }
        if ("KNIGHT".equals(card1.getType()) && card2.isSpell() && "WATER".equals(card2.getElement())) {
            return -1;
        }
        if ("KNIGHT".equals(card2.getType()) && card1.isSpell() && "WATER".equals(card1.getElement())) {
            return 1;
        }
        if ("KRAKEN".equals(card1.getType()) && card2.isSpell()) {
            return 1;
        }
        if ("KRAKEN".equals(card2.getType()) && card1.isSpell()) {
            return -1;
        }
        if ("FIREELF".equals(card1.getType()) && "DRAGON".equals(card2.getType())) {
            return 1;
        }
        if ("FIREELF".equals(card2.getType()) && "DRAGON".equals(card1.getType())) {
            return -1;
        }

        // Effektivität berücksichtigen
        double card1Damage = card1.getDamage();
        double card2Damage = card2.getDamage();

        if (card1.isSpell() || card2.isSpell()) {
            if (card1.isEffectiveAgainst(card2)) {
                card1Damage *= 2;
            } else if (card2.isEffectiveAgainst(card1)) {
                card2Damage *= 2;
            }
        }

        return Double.compare(card1Damage, card2Damage);
    }

    private String concludeBattle(int rounds) {
        if (player1Deck.getCards().isEmpty() && player2Deck.getCards().isEmpty()) {
            return "The battle ended in a draw after " + rounds + " rounds.";
        }

        String winner = player1Deck.getCards().isEmpty() ? "Player 2" : "Player 1";
        return "The battle is over. The winner is " + winner + ".";
    }

    public List<String> getBattleLog() {
        return battleLog;
    }
}
package at.fhtw.MTCG.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Battle {
    private final User player1;
    private final User player2;
    private final List<String> battleLog;

    public Battle(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.battleLog = new ArrayList<>();
    }

    public String startBattle() {
        int round = 0;

        Deck deck1 = player1.getDeck();
        Deck deck2 = player2.getDeck();

        while (!deck1.isEmpty() && !deck2.isEmpty() && round < 100) {
            round++;
            battleLog.add("Round " + round + ":");

            Card card1 = deck1.drawRandomCard();
            Card card2 = deck2.drawRandomCard();

            battleLog.add(player1.getUsername() + " plays " + card1.getName());
            battleLog.add(player2.getUsername() + " plays " + card2.getName());

            int result = fight(card1, card2);

            if (result > 0) {
                deck1.addCard(card2);
                battleLog.add(player1.getUsername() + " wins the round.");
            } else if (result < 0) {
                deck2.addCard(card1);
                battleLog.add(player2.getUsername() + " wins the round.");
            } else {
                deck1.returnCard(card1);
                deck2.returnCard(card2);
                battleLog.add("The round is a draw.");
            }
        }
        return concludeBattle(round, deck1, deck2);
    }

    private int fight(Card card1, Card card2) {
        // Spezialregeln
        if ("GOBLIN".equals(card1.getType()) && "DRAGON".equals(card2.getType())) {
            return -1; // Goblins are too afraid of Dragons to attack.
        }
        if ("GOBLIN".equals(card2.getType()) && "DRAGON".equals(card1.getType())) {
            return 1; // Goblins are too afraid of Dragons to attack.
        }
        if ("KRAKEN".equals(card1.getType()) && card2.isSpell()) {
            return 1; // The Kraken is immune against spells.
        }
        if ("KRAKEN".equals(card2.getType()) && card1.isSpell()) {
            return -1; // The Kraken is immune against spells.
        }
        if ("WIZARD".equals(card1.getType()) && "ORK".equals(card2.getType())) {
            return 1; // Wizzard can control Orks so they are not able to damage them.
        }
        if ("WIZARD".equals(card2.getType()) && "ORK".equals(card1.getType())) {
            return -1; // Wizzard can control Orks so they are not able to damage them.
        }
        if ("KNIGHT".equals(card1.getType()) && card2.isSpell() && "WATER".equals(card2.getElement())) {
            return -1; // The armor of Knights is so heavy that WaterSpells make them drown them instantly.
        }
        if ("KNIGHT".equals(card2.getType()) && card1.isSpell() && "WATER".equals(card1.getElement())) {
            return 1; // The armor of Knights is so heavy that WaterSpells make them drown them instantly.
        }
        if ("FIREELF".equals(card1.getType()) && "DRAGON".equals(card2.getType())) {
            return 1; // The FireElves know Dragons since they were little and can evade their attacks.
        }
        if ("FIREELF".equals(card2.getType()) && "DRAGON".equals(card1.getType())) {
            return -1; // The FireElves know Dragons since they were little and can evade their attacks.
        }

        double card1Damage = card1.getDamage();
        double card2Damage = card2.getDamage();

        if (card1.isSpell() || card2.isSpell()) {
            if (card1.isEffectiveAgainst(card2)){
                card1Damage *= 2;
            } else if (card2.isEffectiveAgainst(card1)) {
                card2Damage *= 2;
            }
        }
        return Double.compare(card1Damage, card2Damage);
    }

    private String concludeBattle(int rounds, Deck deck1, Deck deck2) {
        if (deck1.isEmpty() && deck2.isEmpty()) {
            return "The battle ended in a draw after " + rounds + " rounds.";
        }

        User winner = deck1.isEmpty() ? player2 : player1;
        return "The battle is over. The winner is " + winner.getUsername() + ".";
    }

    public List<String> getBattleLog() {
        return this.battleLog;
    }
}
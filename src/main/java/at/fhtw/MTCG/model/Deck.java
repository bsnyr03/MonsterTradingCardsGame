package at.fhtw.MTCG.model;

import java.util.List;

public class Deck {
    private int id;
    private int userId;
    private List<Card> cards;

    public Deck(int userId, List<Card> cards) {
        this.userId = userId;
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
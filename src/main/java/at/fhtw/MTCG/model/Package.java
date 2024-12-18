package at.fhtw.MTCG.model;

import java.util.List;

public class Package {
    private int id;
    private List<Card> cards;

    public Package(List<Card> cards) {
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
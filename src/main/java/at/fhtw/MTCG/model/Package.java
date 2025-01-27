package at.fhtw.MTCG.model;

import java.util.List;

public class Package {

    private int id;
    private String packageName;
    private List<Card> cards;

    public Package(int id, String packageName, List<Card> cards) {
        this.id = id;
        this.packageName = packageName;
        this.cards = cards;
    }

    public String getName() {
        return packageName;
    }

    public void setName(String packageName) {
        this.packageName = packageName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
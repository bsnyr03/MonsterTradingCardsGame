package at.fhtw.MTCG.model;

import at.fhtw.MTCG.model.enums.CardType;

import java.lang.annotation.ElementType;


public class Card {
    private int id;
    private String name;
    private double damage;
    private ElementType element;
    private CardType type;

    public Card(){}

    public Card(int id, String name, double damage, at.fhtw.MTCG.model.enums.ElementType element, CardType type){
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public ElementType getElement() {
        return element;
    }

    public void setElement(ElementType element) {
        this.element = element;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", damage=" + damage +
                ", element=" + element +
                ", type=" + type +
                '}';
    }
}

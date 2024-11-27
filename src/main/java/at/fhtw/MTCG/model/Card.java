package at.fhtw.MTCG.model;

import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;

import java.lang.annotation.ElementType;


public class Card {
    private int id;
    private String name;
    private double damage;
    private ElementTypeEnum element;
    private CardTypeEnum type;

    public Card(){}

    public Card(int id, String name, double damage, ElementTypeEnum element, CardTypeEnum type){
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

    public ElementTypeEnum getElement() {
        return element;
    }

    public void setElement(ElementTypeEnum element) {
        this.element = element;
    }

    public CardTypeEnum getType() {
        return type;
    }

    public void setType(CardTypeEnum type) {
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

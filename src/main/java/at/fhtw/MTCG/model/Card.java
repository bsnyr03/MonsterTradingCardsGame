package at.fhtw.MTCG.model;

import at.fhtw.MTCG.model.enums.CardTypeEnum;
import at.fhtw.MTCG.model.enums.ElementTypeEnum;
import at.fhtw.MTCG.model.enums.MonsterTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private int id;
    private String name;
    private double damage;
    private ElementTypeEnum element;
    private CardTypeEnum type;
    private MonsterTypeEnum monsterType;


    public Card(){}

    public Card(int id, String name, double damage, ElementTypeEnum element, CardTypeEnum type, MonsterTypeEnum monsterType){
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.element = element;
        this.type = type;
        this.monsterType = monsterType;
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

    public MonsterTypeEnum getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(MonsterTypeEnum monsterType) {
        this.monsterType = monsterType;
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
                ", monsterType=" + monsterType +
                '}';
    }

    public double calculateDamageAgainst(Card opponentCard) {
        if (this.type == CardTypeEnum.MONSTER && opponentCard.type == CardTypeEnum.MONSTER) {
            return this.damage;
        }

        double effectiveness = this.element.calculateEffectiveness(opponentCard.element);
        return this.damage * effectiveness;
    }

    public boolean isSpell() {
        return "SPELL".equalsIgnoreCase(String.valueOf(this.type));
    }

    public boolean isEffectiveAgainst(Card other) {
        if (this.element == ElementTypeEnum.WATER && other.element == ElementTypeEnum.FIRE) {
            return true;
        }
        if (this.element == ElementTypeEnum.FIRE && other.element == ElementTypeEnum.NORMAL) {
            return true;
        }
        if (this.element == ElementTypeEnum.NORMAL && other.element == ElementTypeEnum.WATER) {
            return true;
        }
        return false;
    }

    public boolean isIneffectiveAgainst(Card other) {
        if (this.element == ElementTypeEnum.FIRE && other.element == ElementTypeEnum.WATER) {
            return true;
        }
        if (this.element == ElementTypeEnum.NORMAL && other.element == ElementTypeEnum.FIRE) {
            return true;
        }
        if (this.element == ElementTypeEnum.WATER && other.element == ElementTypeEnum.NORMAL) {
            return true;
        }
        return false;
    }
}

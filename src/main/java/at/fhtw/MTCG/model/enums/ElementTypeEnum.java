package at.fhtw.MTCG.model.enums;

public enum ElementTypeEnum {
    FIRE,
    WATER,
    NORMAL;

    public double calculateEffectiveness(ElementTypeEnum opponentElement) {
        if (this == WATER && opponentElement == FIRE) {
            return 2.0; // Wasser ist effektiv gegen Feuer
        } else if (this == FIRE && opponentElement == NORMAL) {
            return 2.0; // Feuer ist effektiv gegen Normal
        } else if (this == NORMAL && opponentElement == WATER) {
            return 2.0; // Normal ist effektiv gegen Wasser
        } else if (this == FIRE && opponentElement == WATER) {
            return 0.5; // Feuer ist ineffektiv gegen Wasser
        } else if (this == WATER && opponentElement == NORMAL) {
            return 0.5; // Wasser ist ineffektiv gegen Normal
        } else if (this == NORMAL && opponentElement == FIRE) {
            return 0.5; // Normal ist ineffektiv gegen Feuer
        }
        return 1.0; // Keine Effektivität
    }
}
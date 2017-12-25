package utils;

import java.util.Arrays;

/**
 * Klasse für das Spielfeld.
 * Diese Klasse sollte von AIs nicht verwendet werden.
 *
 * @author Gideon
 */
public class GameField {
    /**
     * Das Feld selber.
     */
    public Value[][] field;

    /**
     * Erzeugt ein neues Feld in Abhängigkeit der Feldgröße.
     *
     * @param sizeX Größe in x-Richtung
     * @param sizeY Größe in y-Richtung
     */
    public GameField(int sizeX, int sizeY) {
        this.field = new Value[sizeX][sizeY];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = Value.NONE;
            }
        }
    }

    /**
     * Mögliche Werte der einzelnen Zellen.
     */
    public enum Value {
        NONE, PLAYER1, PLAYER2
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameField gameField = (GameField) o;

        return Arrays.deepEquals(field, gameField.field);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(field);
    }
}

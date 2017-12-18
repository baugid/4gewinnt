package utils;

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

}

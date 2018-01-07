package utils;

import java.io.Serializable;

/**
 * Verwalter eines GameFields.
 * Diese Klasse soll verwendet werden.
 *
 * @author Gideon
 */
public class GameFieldHandle implements Serializable {
    /**
     * Das Feld selber.
     */
    private final GameField f;

    /**
     * Erzeugt einen neuen Wrapper für f.
     *
     * @param f Das Feld, das geschützt werden soll.
     */
    public GameFieldHandle(GameField f) {
        this.f = f;
    }

    /**
     * Gibt den Wert des Feldes an einer Stelle zurück.
     *
     * @param coordX Die x-Koordinate.
     * @param coordY Die y-Koordinate.
     * @return Der Wert des Feldes.
     */
    public GameField.Value getValue(int coordX, int coordY) {
        return f.field[coordX][coordY];
    }

    /**
     * Getter für die Breite.
     *
     * @return Die Breite des Feldes.
     */
    public int getWidth() {
        return f.field.length;
    }

    /**
     * Getter für die Höhe.
     *
     * @return Die Höhe des Feldes.
     */
    public int getHeight() {
        return f.field[0].length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameFieldHandle that = (GameFieldHandle) o;

        return f.equals(that.f);
    }

    @Override
    public int hashCode() {
        return f.hashCode();
    }
}

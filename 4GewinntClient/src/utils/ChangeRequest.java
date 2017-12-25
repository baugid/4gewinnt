package utils;

import static utils.GameField.Value;

/**
 * Diese Klasse repräsentiert eine Änderung des Feldes durch einen Spieler.
 *
 * @author Gideon
 */
public class ChangeRequest {
    private final int x;
    private final int y;
    private final Value player;

    /**
     * Erzeuge ein neues Objekt dieser Klasse.
     *
     * @param x      Die x-Koordinate der Änderung.
     * @param y      Die y-Koordinate der Änderung.
     * @param player Der Spieler, der dort setzen möchte.
     */
    public ChangeRequest(int x, int y, Value player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    /**
     * Getter für x.
     *
     * @return Die x-Koordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter für y.
     *
     * @return Die y-Koordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Getter für den Spieler
     *
     * @return Den Spieler.
     */
    public Value getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeRequest that = (ChangeRequest) o;

        return x == that.x && y == that.y && player == that.player;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + player.hashCode();
        return result;
    }
}

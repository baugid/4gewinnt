package utils;

/**
 * Das Interface für sämtliche AIs
 *
 * @author Gideon
 */
public interface User {
    /**
     * Bereitet die AI auf ein neues Spiel vor.
     *
     * @param player Der Spieler, den die AI in diesem Spiel steuert.
     */
    void init(GameField.Value player);

    /**
     * Diese Methode wird für jeden Zug aufgerufen.
     * Hier muss der zentrale AI Code hin.
     *
     * @param field Das aktuelle Feld.
     * @return Die gewünschte Änderung
     */
    ChangeRequest set(GameFieldHandle field);

    /**
     * Hier soll der Team-/AI-name zurückgegeben werden.
     *
     * @return Den Namen.
     */
    String getName();
}

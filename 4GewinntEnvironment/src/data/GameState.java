package data;

import utils.GameField;

public class GameState {
    private static final int amountInRow = 4;
    private int winsPlayer1 = 0;
    private int winsPlayer2 = 0;
    private int draws = 0;
    private GameField field;


    public GameState(int sizeX, int sizeY) {
        field = new GameField(sizeX, sizeY);
    }

    public int getWinsPlayer1() {
        return winsPlayer1;
    }

    public void setWinsPlayer1(int winsPlayer1) {
        this.winsPlayer1 = winsPlayer1;
    }

    public int getWinsPlayer2() {
        return winsPlayer2;
    }

    public void setWinsPlayer2(int winsPlayer2) {
        this.winsPlayer2 = winsPlayer2;
    }


    public GameField getField() {
        return field;
    }

    public int gamesPlayed() {
        return winsPlayer1 + winsPlayer2 + draws;
    }


    public void resetField() {
        field = new GameField(field.field.length, field.field[0].length);
    }


    public int getAmountInRow() {
        return amountInRow;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setField(GameField field) {
        this.field = field;
    }

    public void reset() {
        winsPlayer1 = 0;
        winsPlayer2 = 0;
        draws = 0;
        resetField();
    }
}

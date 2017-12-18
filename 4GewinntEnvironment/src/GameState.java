import utils.GameField;

import static utils.GameField.Value.*;

class GameState {
    private static final int amountInRow = 4;
    private int winsPlayer1 = 0;
    private int winsPlayer2 = 0;
    private int player1Index;
    private int player2Index;
    private int draws = 0;
    private GameField field;


    public GameState(int sizeX, int sizeY, int player1Index, int player2Index) {
        field = new GameField(sizeX, sizeY);
        this.player1Index = player1Index;
        this.player2Index = player2Index;
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

    public int getPlayer1Index() {
        return player1Index;
    }

    public int getPlayer2Index() {
        return player2Index;
    }

    public GameField getField() {
        return field;
    }

    public int gamesPlayed() {
        return winsPlayer1 + winsPlayer2 + draws;
    }

    public GameField.Value checkWinner() {
        for (int x = 0; x <= field.field.length - amountInRow; x++) {
            for (int y = 0; y <= field.field[x].length - amountInRow; y++) {
                switch (checkSubField(x, y)) {
                    case PLAYER1:
                        winsPlayer1++;
                        return PLAYER1;
                    case PLAYER2:
                        winsPlayer2++;
                        return PLAYER2;
                }
            }
        }
        if (isFull()) {
            draws++;
            return NONE;
        }
        return null;
    }

    public void resetField() {
        field = new GameField(field.field.length, field.field[0].length);
    }

    public void swapPlayers() {
        int help = winsPlayer1;
        winsPlayer1 = winsPlayer2;
        winsPlayer2 = help;
        help = player1Index;
        player1Index = player2Index;
        player2Index = help;
    }

    private GameField.Value checkColumn(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = y + 1; i < y + amountInRow; i++) {
            if (field.field[x][i] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkRow(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + amountInRow; i++) {
            if (field.field[i][y] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkFallingDiagonal(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + amountInRow; i++) {
            if (field.field[i][i] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkRisingDiagonal(int x, int y) {
        GameField.Value base = field.field[x][y + (amountInRow - 1)];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + amountInRow; i++) {
            if (field.field[i][y + (amountInRow - 1) - i + x] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkSubField(int x, int y) {
        //All Columns
        for (int i = x; i < x + amountInRow; i++) {
            switch (checkColumn(i, y)) {
                case PLAYER1:
                    return PLAYER1;
                case PLAYER2:
                    return PLAYER2;
            }
        }
        //All Rows
        for (int i = y; i < y + amountInRow; i++) {
            switch (checkRow(x, i)) {
                case PLAYER1:
                    return PLAYER1;
                case PLAYER2:
                    return PLAYER2;
            }
        }
        switch (checkFallingDiagonal(x, y)) {
            case PLAYER1:
                return PLAYER1;
            case PLAYER2:
                return PLAYER2;
        }
        return checkRisingDiagonal(x, y);
    }

    private boolean isFull() {
        for (GameField.Value[] vs : field.field) {
            for (GameField.Value v : vs) {
                if (v == GameField.Value.NONE)
                    return false;
            }
        }
        return true;
    }

    public void reset() {
        player1Index = 0;
        player2Index = 1;
        winsPlayer1 = 0;
        winsPlayer2 = 0;
        draws = 0;
        resetField();
    }
}

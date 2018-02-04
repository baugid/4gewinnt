package control;

import utils.GameField;

import static utils.GameField.Value.*;

class WinChecker extends ControlElement {
    private GameField field;

    private static WinChecker instance = new WinChecker();

    private WinChecker() {
    }

    public static WinChecker getInstance() {
        return instance;
    }

    public void declareWinner() {
        GameField.Value winner = checkWinner();
        if (winner == null) return;
        switch (winner) {
            case NONE:
                getPoints().set(players.getPlayer1Index(), getPoints().get(players.getPlayer1Index()) + 1);
                getPoints().set(players.getPlayer2Index(), getPoints().get(players.getPlayer2Index()) + 1);
                break;
            case PLAYER1:
                getPoints().set(players.getPlayer1Index(), getPoints().get(players.getPlayer1Index()) + 3);
                break;
            case PLAYER2:
                getPoints().set(players.getPlayer2Index(), getPoints().get(players.getPlayer2Index()) + 3);
                break;
        }
        swapPlayers();
        updateField();
    }

    private GameField.Value checkWinner() {
        field = state.getField();
        for (int x = 0; x <= field.field.length - state.getAmountInRow(); x++) {
            for (int y = 0; y <= field.field[x].length - state.getAmountInRow(); y++) {
                switch (checkSubField(x, y)) {
                    case PLAYER1:
                        state.setWinsPlayer1(state.getWinsPlayer1() + 1);
                        return PLAYER1;
                    case PLAYER2:
                        state.setWinsPlayer2(state.getWinsPlayer2() + 1);
                        return PLAYER2;
                }
            }
        }
        if (isFull()) {
            state.setDraws(state.getDraws() + 1);
            return NONE;
        }
        return null;
    }

    private GameField.Value checkColumn(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = y + 1; i < y + state.getAmountInRow(); i++) {
            if (field.field[x][i] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkRow(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + state.getAmountInRow(); i++) {
            if (field.field[i][y] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkFallingDiagonal(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + state.getAmountInRow(); i++) {
            if (field.field[i][i] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkRisingDiagonal(int x, int y) {
        GameField.Value base = field.field[x][y + (state.getAmountInRow() - 1)];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + state.getAmountInRow(); i++) {
            if (field.field[i][y + (state.getAmountInRow() - 1) - i + x] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkSubField(int x, int y) {
        //All Columns
        for (int i = x; i < x + state.getAmountInRow(); i++) {
            switch (checkColumn(i, y)) {
                case PLAYER1:
                    return PLAYER1;
                case PLAYER2:
                    return PLAYER2;
            }
        }
        //All Rows
        for (int i = y; i < y + state.getAmountInRow(); i++) {
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

}

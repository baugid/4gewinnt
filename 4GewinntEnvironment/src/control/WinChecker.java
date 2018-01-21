package control;

import utils.GameField;

import static utils.GameField.Value.*;

class WinChecker {
    private ControlState controlState;
    private GameField field;

    WinChecker(ControlState state) {
        controlState = state;
    }

    public void declareWinner() {
        GameField.Value winner = checkWinner();
        if (winner == null) return;
        switch (winner) {
            case NONE:
                controlState.points.set(controlState.players.getPlayer1Index(), controlState.points.get(controlState.players.getPlayer1Index()) + 1);
                controlState.points.set(controlState.players.getPlayer2Index(), controlState.points.get(controlState.players.getPlayer2Index()) + 1);
                controlState.d.printGameState(controlState.state.getField());
                controlState.players.swapPlayers();
                break;
            case PLAYER1:
                controlState.points.set(controlState.players.getPlayer1Index(), controlState.points.get(controlState.players.getPlayer1Index()) + 3);
                controlState.d.printGameState(controlState.state.getField());
                controlState.players.swapPlayers();
                break;
            case PLAYER2:
                controlState.points.set(controlState.players.getPlayer2Index(), controlState.points.get(controlState.players.getPlayer2Index()) + 3);
                controlState.d.printGameState(controlState.state.getField());
                controlState.players.swapPlayers();
                break;
        }
    }

    private GameField.Value checkWinner() {
        field = controlState.state.getField();
        for (int x = 0; x <= field.field.length - controlState.state.getAmountInRow(); x++) {
            for (int y = 0; y <= field.field[x].length - controlState.state.getAmountInRow(); y++) {
                switch (checkSubField(x, y)) {
                    case PLAYER1:
                        controlState.state.setWinsPlayer1(controlState.state.getWinsPlayer1() + 1);
                        return PLAYER1;
                    case PLAYER2:
                        controlState.state.setWinsPlayer2(controlState.state.getWinsPlayer2() + 1);
                        return PLAYER2;
                }
            }
        }
        if (isFull()) {
            controlState.state.setDraws(controlState.state.getDraws() + 1);
            return NONE;
        }
        return null;
    }

    private GameField.Value checkColumn(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = y + 1; i < y + controlState.state.getAmountInRow(); i++) {
            if (field.field[x][i] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkRow(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + controlState.state.getAmountInRow(); i++) {
            if (field.field[i][y] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkFallingDiagonal(int x, int y) {
        GameField.Value base = field.field[x][y];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + controlState.state.getAmountInRow(); i++) {
            if (field.field[i][i] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkRisingDiagonal(int x, int y) {
        GameField.Value base = field.field[x][y + (controlState.state.getAmountInRow() - 1)];
        if (base == NONE) return NONE;
        for (int i = x + 1; i < x + controlState.state.getAmountInRow(); i++) {
            if (field.field[i][y + (controlState.state.getAmountInRow() - 1) - i + x] != base)
                return NONE;
        }
        return base;
    }

    private GameField.Value checkSubField(int x, int y) {
        //All Columns
        for (int i = x; i < x + controlState.state.getAmountInRow(); i++) {
            switch (checkColumn(i, y)) {
                case PLAYER1:
                    return PLAYER1;
                case PLAYER2:
                    return PLAYER2;
            }
        }
        //All Rows
        for (int i = y; i < y + controlState.state.getAmountInRow(); i++) {
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

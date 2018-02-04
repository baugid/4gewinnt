package control;

import utils.ChangeRequest;
import utils.GameField;
import utils.GameFieldHandle;

import static utils.GameField.Value.*;

class Calculator extends ControlElement implements Runnable {
    private boolean firstPlayersTurn = true;

    private static Calculator instance = new Calculator();

    private Calculator() {
    }

    public static Calculator getInstance() {
        return instance;
    }

    private boolean checkIfInvalidRequest(ChangeRequest request, GameField.Value Player) {
        return request == null || request.getPlayer() != Player || request.getY() < 0 || request.getX() < 0 || request.getY() >= state.getField().field[0].length || request.getX() >= state.getField().field.length || state.getField().field[request.getX()][request.getY()] != NONE;
    }

    private boolean endRound() {
        int newP1 = players.getPlayer1Index(), newP2 = players.getPlayer2Index();
        if (newP1 + 2 == players.listPlayers().size()) {
            end();
            return false;
        } else {
            if (newP2 + 1 == players.listPlayers().size()) {
                ++newP1;
                newP2 = newP1 + 1;
            } else {
                ++newP2;
            }
        }
        firstPlayersTurn = true;
        state.reset();
        players.setPlayer1(newP1);
        players.setPlayer2(newP2);
        updateTitle();
        return true;
    }

    @Override
    public void run() {
        checker.declareWinner();
        if (state.gamesPlayed() == 100)
            if (!endRound()) return;

        ChangeRequest change;
        if (firstPlayersTurn) {
            change = players.getPlayer1().set(new GameFieldHandle(state.getField()));
            if (checkIfInvalidRequest(change, PLAYER1)) {
                state.setWinsPlayer2(state.getWinsPlayer2() + 1);
                swapPlayers();
                return;
            }
        } else {
            change = players.getPlayer2().set(new GameFieldHandle(state.getField()));
            if (checkIfInvalidRequest(change, PLAYER2)) {
                state.setWinsPlayer1(state.getWinsPlayer1() + 1);
                swapPlayers();
                return;
            }
        }
        firstPlayersTurn = !firstPlayersTurn;
        state.getField().field[change.getX()][change.getY()] = change.getPlayer();
        if (slowMode) updateField();
    }
}

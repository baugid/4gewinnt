package control;

import utils.ChangeRequest;
import utils.GameField;
import utils.GameFieldHandle;

import static utils.GameField.Value.*;

class Calculator implements Runnable {
    private ControlState controlState;
    private boolean firstPlayersTurn = true;

    Calculator(ControlState controlState) {
        this.controlState = controlState;
    }

    private boolean checkIfInvalidRequest(ChangeRequest request, GameField.Value Player) {
        return request == null || request.getPlayer() != Player || request.getY() < 0 || request.getX() < 0 || request.getY() >= controlState.state.getField().field[0].length || request.getX() >= controlState.state.getField().field.length || controlState.state.getField().field[request.getX()][request.getY()] != NONE;
    }

    private boolean endRound() {
        int newP1 = controlState.players.getPlayer1Index(), newP2 = controlState.players.getPlayer2Index();
        if (newP1 + 2 == controlState.players.listPlayers().size()) {
            controlState.c.end();
            return false;
        } else {
            if (newP2 + 1 == controlState.players.listPlayers().size()) {
                ++newP1;
                newP2 = newP1 + 1;
            } else {
                ++newP2;
            }
        }
        firstPlayersTurn = true;
        controlState.state.reset();
        controlState.players.setPlayer1(newP1);
        controlState.players.setPlayer2(newP2);
        controlState.d.displayText(controlState.players.getPlayer1().getName() + " vs. " + controlState.players.getPlayer2().getName() + " " + controlState.state.getWinsPlayer1() + ":" + controlState.state.getWinsPlayer2());
        return true;
    }

    @Override
    public void run() {
        controlState.checker.declareWinner();
        if (controlState.state.gamesPlayed() == 100)
            if (!endRound()) return;

        ChangeRequest change;
        if (firstPlayersTurn) {
            change = controlState.players.getPlayer1().set(new GameFieldHandle(controlState.state.getField()));
            if (checkIfInvalidRequest(change, PLAYER1)) {
                controlState.state.setWinsPlayer2(controlState.state.getWinsPlayer2() + 1);
                controlState.players.swapPlayers();
                return;
            }
        } else {
            change = controlState.players.getPlayer2().set(new GameFieldHandle(controlState.state.getField()));
            if (checkIfInvalidRequest(change, PLAYER2)) {
                controlState.state.setWinsPlayer1(controlState.state.getWinsPlayer1() + 1);
                controlState.players.swapPlayers();
                return;
            }
        }
        firstPlayersTurn = !firstPlayersTurn;
        controlState.state.getField().field[change.getX()][change.getY()] = change.getPlayer();
        if (controlState.slowMode) controlState.d.printGameState(controlState.state.getField());
    }
}

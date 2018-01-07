import utils.ChangeRequest;
import utils.GameFieldHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static utils.GameField.Value;
import static utils.GameField.Value.*;

public class Game {
    private Players player;
    private boolean isRunning = false;
    private boolean slowMode = true;
    private Drawer drawer;
    private double speed = 1;
    private ScheduledExecutorService calcThread;
    private ScheduledFuture<?> calcControl = null;
    private ArrayList<Integer> points;
    private GameState state;
    private boolean firstPlayersTurn = true;

    public Game(Drawer d) {
        player = new Players();
        drawer = d;
        state = new GameState(5, 5, 0, 1);
        calcThread = Executors.newSingleThreadScheduledExecutor();
        points = new ArrayList<>();
    }

    public void addClient(File client) {
        if (player.loadPlayer(client))
            points.add(0);
    }

    private void resubmitTask() {
        if (calcControl != null) {
            calcControl.cancel(false);
            calcControl = calcThread.scheduleWithFixedDelay(this::calcStep, calcControl.getDelay(TimeUnit.MICROSECONDS) > (long) (1000000.0 / speed) ? (long) (1000000.0 / speed) : calcControl.getDelay(TimeUnit.MICROSECONDS), slowMode ? (long) (1000000.0 / speed) : 1L, TimeUnit.MICROSECONDS);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isSlow() {
        return slowMode;
    }

    public void start() {
        if (player.getUserCount() < 2) {
            drawer.stop();
            return;
        }
        if (state.gamesPlayed() == 0 && state.getPlayer1Index() == 0 && state.getPlayer2Index() == 1) {
            player.getPlayer(0).init(PLAYER1);
            player.getPlayer(1).init(PLAYER2);
        }
        drawer.displayText(player.getPlayer(state.getPlayer1Index()).getName() + " vs. " + player.getPlayer(state.getPlayer2Index()).getName() + " " + state.getWinsPlayer1() + ":" + state.getWinsPlayer2());
        if (calcControl != null)
            resubmitTask();
        else
            calcControl = calcThread.scheduleWithFixedDelay(this::calcStep, 0, slowMode ? (long) (long) (1000000.0 / speed) : 1L, TimeUnit.MICROSECONDS);
        isRunning = true;
    }

    public void pause() {
        if (calcControl != null)
            calcControl.cancel(false);
        calcControl = null;
        isRunning = false;
    }

    public void stop() {
        reset();
        isRunning = false;
    }

    private void reset() {
        if (calcControl != null)
            calcControl.cancel(true);
        calcControl = null;
        state.reset();
        drawer.stop();
        points = new ArrayList<>();
        for (int i = 0; i < player.getUserCount(); i++) points.add(0);
        drawer.printGameState(state.getField());
        if (player.getUserCount() > 2)
            drawer.displayText(player.getPlayer(state.getPlayer1Index()).getName() + " vs. " + player.getPlayer(state.getPlayer2Index()).getName() + " " + state.getWinsPlayer1() + ":" + state.getWinsPlayer2());
    }

    public void toggleMode() {
        slowMode = !slowMode;
        resubmitTask();
    }

    public void requestRepaint() {
        drawer.printGameState(state.getField());
    }

    public void setSpeed(double val) {
        speed = val;
        if (slowMode)
            resubmitTask();
    }

    private void end() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>(points);
        for (int i = 0; i < player.getUserCount(); i++) {
            names.add(player.getPlayer(i).getName());
        }
        stop();
        drawer.displayResult(scores, names);
    }

    private boolean checkIfInvalidRequest(ChangeRequest request, Value Player) {
        return request == null || request.getPlayer() != Player || request.getY() < 0 || request.getX() < 0 || request.getY() >= state.getField().field[0].length || request.getX() >= state.getField().field.length || state.getField().field[request.getX()][request.getY()] != NONE;
    }

    private void calcStep() {
        declareWinner();
        if (state.gamesPlayed() == 100) {
            int newP1 = state.getPlayer1Index(), newP2 = state.getPlayer2Index();
            if (newP1 + 2 == player.getUserCount()) {
                end();
                return;
            } else {
                if (newP2 + 1 == player.getUserCount()) {
                    ++newP1;
                    newP2 = newP1 + 1;
                } else {
                    ++newP2;
                }
            }
            state = new GameState(5, 5, newP1, newP2);
            player.getPlayer(newP1).init(PLAYER1);
            player.getPlayer(newP2).init(PLAYER2);
            drawer.displayText(player.getPlayer(state.getPlayer1Index()).getName() + " vs. " + player.getPlayer(state.getPlayer2Index()).getName() + " " + state.getWinsPlayer1() + ":" + state.getWinsPlayer2());
        }
        ChangeRequest change;
        if (firstPlayersTurn) {
            change = player.getPlayer(state.getPlayer1Index()).set(new GameFieldHandle(state.getField()));
            if (checkIfInvalidRequest(change, PLAYER1)) {
                state.setWinsPlayer2(state.getWinsPlayer2() + 1);
                swapPlayers();
                return;
            }
        } else {
            change = player.getPlayer(state.getPlayer2Index()).set(new GameFieldHandle(state.getField()));
            if (checkIfInvalidRequest(change, PLAYER2)) {
                state.setWinsPlayer1(state.getWinsPlayer1() + 1);
                swapPlayers();
                return;
            }
        }
        firstPlayersTurn = !firstPlayersTurn;
        state.getField().field[change.getX()][change.getY()] = change.getPlayer();
        if (slowMode) requestRepaint();
    }

    private void declareWinner() {
        Value winner = state.checkWinner();
        if (winner == null) return;
        switch (winner) {
            case NONE:
                points.set(state.getPlayer1Index(), points.get(state.getPlayer1Index()) + 1);
                points.set(state.getPlayer2Index(), points.get(state.getPlayer2Index()) + 1);
                if (!slowMode) requestRepaint();
                swapPlayers();
                break;
            case PLAYER1:
                points.set(state.getPlayer1Index(), points.get(state.getPlayer1Index()) + 3);
                if (!slowMode) requestRepaint();
                swapPlayers();
                break;
            case PLAYER2:
                points.set(state.getPlayer2Index(), points.get(state.getPlayer2Index()) + 3);
                if (!slowMode) requestRepaint();
                swapPlayers();
                break;
        }
    }

    private void swapPlayers() {
        drawer.displayText(player.getPlayer(state.getPlayer1Index()).getName() + " vs. " + player.getPlayer(state.getPlayer2Index()).getName() + " " + state.getWinsPlayer1() + ":" + state.getWinsPlayer2());
        state.swapPlayers();
        player.getPlayer(state.getPlayer1Index()).init(PLAYER1);
        player.getPlayer(state.getPlayer2Index()).init(PLAYER2);
        state.resetField();
        firstPlayersTurn = true;
    }

}

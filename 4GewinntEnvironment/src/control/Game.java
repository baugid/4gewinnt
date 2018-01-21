package control;

import data.PlayerHandler;
import gui.Drawer;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static utils.GameField.Value.PLAYER1;
import static utils.GameField.Value.PLAYER2;

public class Game implements MainController {
    private double speed = 1;
    private ScheduledExecutorService calcThread;
    private ScheduledFuture<?> calcControl = null;
    private ControlState controlState;

    public Game(Drawer d) {
        controlState = new ControlState(5, 5, d, this);
        calcThread = Executors.newSingleThreadScheduledExecutor();
    }

    public void addClient(File client) {
        if (controlState.players.addPlayer(client))
            controlState.points.add(0);
    }

    private void resubmitTask() {
        if (calcControl != null) {
            calcControl.cancel(false);
            calcControl = calcThread.scheduleWithFixedDelay(controlState.calc, calcControl.getDelay(TimeUnit.MICROSECONDS) > (long) (1000000.0 / speed) ? (long) (1000000.0 / speed) : calcControl.getDelay(TimeUnit.MICROSECONDS), controlState.slowMode ? (long) (1000000.0 / speed) : 1L, TimeUnit.MICROSECONDS);
        }
    }

    public boolean isRunning() {
        return controlState.isRunning;
    }

    public boolean isSlow() {
        return controlState.slowMode;
    }

    @Override
    public void start() {
        if (controlState.players.listPlayers().size() < 2) {
            controlState.d.stop();
            return;
        }
        if (controlState.state.gamesPlayed() == 0 && controlState.players.getPlayer1Index() == 0 && controlState.players.getPlayer2Index() == 1) {
            controlState.players.getPlayer1().init(PLAYER1);
            controlState.players.getPlayer2().init(PLAYER2);
        }
        controlState.d.displayText(controlState.players.getPlayer1().getName() + " vs. " + controlState.players.getPlayer2().getName() + " " + controlState.state.getWinsPlayer1() + ":" + controlState.state.getWinsPlayer2());
        if (calcControl != null)
            resubmitTask();
        else
            calcControl = calcThread.scheduleWithFixedDelay(controlState.calc, 0, controlState.slowMode ? (long) (long) (1000000.0 / speed) : 1L, TimeUnit.MICROSECONDS);
        controlState.isRunning = true;
    }

    public void pause() {
        controlState.isRunning = false;
        if (calcControl != null)
            calcControl.cancel(false);
        calcControl = null;
    }

    @Override
    public void stop() {
        controlState.isRunning = false;
        reset();
    }

    @Override
    public void end() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < controlState.players.listPlayers().size(); i++) {
            names.add(controlState.players.listPlayers().get(i).getName());
        }
        controlState.d.displayResult(new ArrayList<>(controlState.points), names);
        stop();
    }

    public void reset() {
        if (calcControl != null)
            calcControl.cancel(true);
        calcControl = null;
        controlState.state.reset();
        controlState.d.stop();
        controlState.points.clear();
        for (int i = 0; i < controlState.players.listPlayers().size(); i++) controlState.points.add(0);
        controlState.d.printGameState(controlState.state.getField());
        if (controlState.players.listPlayers().size() >= 2) {
            controlState.players.setPlayer1(0);
            controlState.players.setPlayer2(1);
            controlState.d.displayText(controlState.players.getPlayer1().getName() + " vs. " + controlState.players.getPlayer2().getName() + " " + controlState.state.getWinsPlayer1() + ":" + controlState.state.getWinsPlayer2());
        }
    }

    public void toggleMode() {
        controlState.slowMode = !controlState.slowMode;
        resubmitTask();
    }

    public void requestRepaint() {
        controlState.d.printGameState(controlState.state.getField());
    }

    public void setSpeed(double val) {
        speed = val;
        if (controlState.slowMode)
            resubmitTask();
    }

    public PlayerHandler getPlayerHandler() {
        return controlState.players;
    }
}

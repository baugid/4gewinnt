package control;

import gui.Drawer;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static utils.GameField.Value.PLAYER1;
import static utils.GameField.Value.PLAYER2;

public class Game extends ControlElement implements ExecutionController {
    private double speed = 1;
    private ScheduledExecutorService calcThread;
    private ScheduledFuture<?> calcControl = null;

    public Game(Drawer d) {
        init(5, 5, d, this);
        calcThread = Executors.newSingleThreadScheduledExecutor();
    }

    public void addClient(File client) {
        if (players.addPlayer(client))
            getPoints().add(0);
    }

    private void resubmitTask() {
        if (calcControl != null) {
            calcControl.cancel(false);
            calcControl = calcThread.scheduleWithFixedDelay(calc, calcControl.getDelay(TimeUnit.MICROSECONDS) > (long) (1000000.0 / speed) ? (long) (1000000.0 / speed) : calcControl.getDelay(TimeUnit.MICROSECONDS), slowMode ? (long) (1000000.0 / speed) : 1L, TimeUnit.MICROSECONDS);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isSlow() {
        return slowMode;
    }

    public void start() {
        if (players.listPlayers().size() < 2) {
            stop();
            return;
        }
        if (state.gamesPlayed() == 0 && players.getPlayer1Index() == 0 && players.getPlayer2Index() == 1) {
            players.getPlayer1().init(PLAYER1);
            players.getPlayer2().init(PLAYER2);
        }
        updateTitle();
        if (calcControl != null)
            resubmitTask();
        else
            calcControl = calcThread.scheduleWithFixedDelay(calc, 0, slowMode ? (long) (long) (1000000.0 / speed) : 1L, TimeUnit.MICROSECONDS);
        isRunning = true;
    }

    public void pause() {
        isRunning = false;
        endExecution();
    }

    public void toggleMode() {
        slowMode = !slowMode;
        resubmitTask();
    }

    public void requestRepaint() {
        updateField();
    }

    public void setSpeed(double val) {
        speed = val;
        if (slowMode)
            resubmitTask();
    }

    public PlayerHandler getPlayerHandler() {
        return players;
    }

    @Override
    public void endExecution() {
        if (calcControl != null)
            calcControl.cancel(true);
        calcControl = null;
    }

    public void stop() {
        reset();
    }
}

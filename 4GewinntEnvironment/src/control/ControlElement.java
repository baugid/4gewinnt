package control;

import data.GameState;
import data.PlayerController;
import gui.Drawer;

import java.util.ArrayList;

abstract class ControlElement {
    static WinChecker checker;
    static GameState state;
    static PlayerController players;
    static Calculator calc;
    static boolean isRunning = false;
    static boolean slowMode = true;
    private static Drawer d;
    private static ExecutionController c;

    static void init(int sizeX, int sizeY, Drawer drawer, ExecutionController controller) {
        state = new GameState(sizeX, sizeY);
        players = new PlayerController(state);
        d = drawer;
        c = controller;
        checker = WinChecker.getInstance();
        calc = Calculator.getInstance();
    }

    final void swapPlayers() {
        players.swapPlayers();
        updateTitle();
    }

    final ArrayList<Integer> getPoints() {
        return players.getPoints();
    }

    final void updateTitle() {
        d.displayText(players.getPlayer1().getName() + " vs. " + players.getPlayer2().getName() + " " + state.getWinsPlayer1() + ":" + state.getWinsPlayer2());
    }

    final void updateField() {
        d.printGameState(state.getField());
    }

    final void reset() {
        isRunning = false;
        c.endExecution();
        state.reset();
        d.stop();
        getPoints().clear();
        for (int i = 0; i < players.listPlayers().size(); i++) getPoints().add(0);
        d.printGameState(state.getField());
        if (players.listPlayers().size() >= 2) {
            players.setPlayer1(0);
            players.setPlayer2(1);
            updateTitle();
        }
    }

    final void end() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < players.listPlayers().size(); i++) {
            names.add(players.listPlayers().get(i).getName());
        }
        d.displayResult(new ArrayList<>(getPoints()), names);
        reset();
    }
}

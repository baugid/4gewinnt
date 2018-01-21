package control;

import data.GameState;
import gui.Drawer;

import java.util.ArrayList;

class ControlState {
    WinChecker checker;
    GameState state;
    PlayerController players;
    ArrayList<Integer> points;
    Calculator calc;
    boolean isRunning = false;
    boolean slowMode = true;
    Drawer d;
    MainController c;

    ControlState(int sizeX, int sizeY, Drawer d, MainController c) {
        checker = new WinChecker(this);
        state = new GameState(sizeX, sizeY);
        points = new ArrayList<>();
        players = new PlayerController(this);
        calc = new Calculator(this);
        this.d = d;
        this.c = c;
    }

}

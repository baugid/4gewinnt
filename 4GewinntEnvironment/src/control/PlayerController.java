package control;

import data.PlayerHandler;
import data.Players;
import utils.GameField;
import utils.User;

import java.io.File;
import java.util.List;

class PlayerController implements PlayerHandler {
    private Players p;
    private ControlState controlState;
    private int player1Index = 0;
    private int player2Index = 1;

    PlayerController(ControlState controlState) {
        this.controlState = controlState;
        p = new Players();
    }

    @Override
    public boolean addPlayer(File f) {
        return p.addPlayer(f);
    }

    @Override
    public boolean removePlayer(User u) {
        int idx = p.listPlayers().indexOf(u);
        return idx >= 0 && removePlayer(idx);
    }

    @Override
    public boolean removePlayer(int index) {
        if (index < 0 || index >= p.listPlayers().size()) return false;
        controlState.points.remove(index);
        p.listPlayers().remove(index);
        return true;
    }

    @Override
    public List<User> listPlayers() {
        return p.listPlayers();
    }

    public User getPlayer1() {
        return p.getPlayer(player1Index);
    }

    public void setPlayer1(int index) {
        player1Index = index;
        getPlayer1().init(GameField.Value.PLAYER1);
    }

    public User getPlayer2() {
        return p.getPlayer(player2Index);
    }

    public void setPlayer2(int index) {
        player2Index = index;
        getPlayer2().init(GameField.Value.PLAYER2);
    }

    public void swapPlayers() {
        int help = controlState.state.getWinsPlayer1();
        controlState.state.setWinsPlayer1(controlState.state.getWinsPlayer2());
        controlState.state.setWinsPlayer2(help);
        help = player1Index;
        player1Index = player2Index;
        player2Index = help;
        controlState.state.resetField();
        getPlayer1().init(GameField.Value.PLAYER1);
        getPlayer2().init(GameField.Value.PLAYER2);
        controlState.d.displayText(controlState.players.getPlayer1().getName() + " vs. " + controlState.players.getPlayer2().getName() + " " + controlState.state.getWinsPlayer1() + ":" + controlState.state.getWinsPlayer2());
    }

    public int getPlayer1Index() {
        return player1Index;
    }

    public int getPlayer2Index() {
        return player2Index;
    }
}

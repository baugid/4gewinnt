package gui;

import javafx.scene.control.MenuBar;

public class ControlMenu extends MenuBar {
    private ClickableMenu start = new ClickableMenu("Start");
    private ClickableMenu stop = new ClickableMenu("Stop");
    private ClickableMenu slow = new ClickableMenu("Fast");
    private ClickableMenu addPlayer = new ClickableMenu("Add AI");
    private ClickableMenu speedSlider = new ClickableMenu("Speed");
    private ClickableMenu listPlayers = new ClickableMenu("All Players");

    ControlMenu(MenuBarHandler callback) {
        start.setOnAction(callback::startEvt);
        stop.setOnAction(callback::stopEvt);
        slow.setOnAction(callback::slowEvt);
        addPlayer.setOnAction(callback::addEvt);
        speedSlider.setOnAction(callback::speedEvt);
        listPlayers.setOnAction(callback::listEvt);
        super.getMenus().addAll(start, stop, slow, addPlayer, speedSlider, listPlayers);
    }

    public ClickableMenu getStart() {
        return start;
    }

    public ClickableMenu getSlow() {
        return slow;
    }
}

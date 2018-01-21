package gui;

import javafx.event.ActionEvent;

public interface MenuBarHandler {
    void startEvt(ActionEvent evt);

    void stopEvt(ActionEvent evt);

    void slowEvt(ActionEvent evt);

    void addEvt(ActionEvent evt);

    void speedEvt(ActionEvent evt);

    void listEvt(ActionEvent evt);
}

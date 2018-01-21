package gui;

import control.Stoppable;
import utils.GameField;

import java.util.ArrayList;

public interface Drawer extends Stoppable {
    void displayText(String text);

    void printGameState(GameField field);

    void displayResult(ArrayList<Integer> scoreList, ArrayList<String> nameList);
}

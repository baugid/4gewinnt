import utils.GameField;

import java.util.ArrayList;

public interface Drawer {
    void displayText(String text);

    void printGameState(GameField field);

    void stop();

    void displayResult(ArrayList<Integer> scoreList, ArrayList<String> nameList);
}

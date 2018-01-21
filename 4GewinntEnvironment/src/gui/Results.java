package gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

public class Results {
    private Stage s;
    private VBox layout;

    Results() {
        s = new Stage();
        s.setTitle("Result");
        s.initModality(Modality.APPLICATION_MODAL);
        layout = new VBox();
        s.setScene(new Scene(layout));
    }

    public void show(ArrayList<Integer> scoreList, ArrayList<String> nameList) {
        layout.getChildren().clear();
        while (scoreList.size() > 0) {
            int score = Collections.max(scoreList);
            int index = scoreList.indexOf(score);
            layout.getChildren().add(new Label(nameList.get(index) + " - " + score));
            scoreList.remove(index);
            nameList.remove(index);
        }
        s.show();
    }
}

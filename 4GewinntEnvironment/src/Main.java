import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.GameField;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application implements Drawer, SpeedObject {

    private Stage window;
    private Game g;
    private Canvas gameArea;
    private SpeedReader s;
    private double barHeight;
    private ClickableMenu startButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("4 in a Row - No game running!");
        window.setResizable(true);
        g = new Game(this);
        BorderPane pane = new BorderPane();
        gameArea = new Canvas() {
            @Override
            public boolean isResizable() {
                return true;
            }
        };
        gameArea.widthProperty().bind(pane.widthProperty());
        gameArea.heightProperty().bind(pane.heightProperty());
        gameArea.widthProperty().addListener((observable, oldValue, newValue) -> g.requestRepaint());
        gameArea.heightProperty().addListener((observable, oldValue, newValue) -> g.requestRepaint());
        MenuBar m = genMenuBar();
        pane.setTop(m);
        pane.setCenter(gameArea);
        Scene mainScene = new Scene(pane, 400, 200);
        window.setOnCloseRequest(evt -> {
            if (s != null && s.isShown()) s.hide();
            g.stop();
            System.exit(0);
        });
        window.setScene(mainScene);
        selectClassFiles(null);
        window.show();
        barHeight = m.getHeight();
        g.requestRepaint();
    }

    private MenuBar genMenuBar() {
        startButton = new ClickableMenu("Start");
        ClickableMenu stop = new ClickableMenu("Stop");
        ClickableMenu slow = new ClickableMenu("Fast");
        ClickableMenu addPlayer = new ClickableMenu("Add AI");
        ClickableMenu speedSlider = new ClickableMenu("Speed");

        startButton.setOnAction(evt -> {
            if (g.isRunning()) {
                startButton.setTitle("Start");
                g.pause();
            } else {
                startButton.setTitle("Pause");
                g.start();
            }
        });

        stop.setOnAction(evt -> {
            if (g.isRunning()) {
                g.stop();
                startButton.setTitle("Start");
            }
        });

        slow.setOnAction(evt -> {
            if (g.isSlow()) {
                slow.setTitle("Slow");
            } else {
                slow.setTitle("Fast");
            }
            g.toggleMode();
        });

        addPlayer.setOnAction(evt -> selectClassFiles(window));

        speedSlider.setOnAction(evt -> {
            if (s == null) {
                s = new SpeedReader(this);
                s.show();
            } else {
                if (s.isShown()) {
                    s.hide();
                } else {
                    s.show();
                }
            }
        });

        return new MenuBar(startButton, stop, slow, addPlayer, speedSlider);
    }

    private void selectClassFiles(Stage w) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Enter AI-File!");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Java Classes", "*.class"));
        File f;
        do {
            f = chooser.showOpenDialog(w);
            if (f != null) {
                g.addClient(f);
            } else
                return;
        } while (true);
    }

    @Override
    public void displayText(String text) {
        Platform.runLater(() -> window.setTitle("4 in a Row - " + text));
    }

    @Override
    public void printGameState(GameField field) {
        Platform.runLater(() -> {
            GraphicsContext ct = gameArea.getGraphicsContext2D();
            ct.clearRect(0, 0, gameArea.getWidth(), gameArea.getHeight());
            double fieldWidth = gameArea.getWidth() / field.field[0].length;
            double fieldHeight = (gameArea.getHeight() - barHeight) / field.field.length;
            double posX = 0;
            double posY = 0;
            ct.setStroke(Color.BLACK);
            for (int i = 0; i < field.field.length - 1; i++) {
                posX += fieldWidth;
                ct.strokeLine(posX, 0, posX, gameArea.getHeight());
            }
            for (int i = 0; i < field.field[0].length - 1; i++) {
                posY += fieldHeight;
                ct.strokeLine(0, posY, gameArea.getWidth(), posY);
            }
            posX = 0;
            for (int i = 0; i < field.field.length; i++) {
                posY = 0;
                for (int j = 0; j < field.field[i].length; j++) {
                    if (field.field[i][j] == GameField.Value.NONE) {
                        posY += fieldHeight;
                        continue;
                    }
                    ct.setFill(field.field[i][j] == GameField.Value.PLAYER1 ? Color.BLUE : Color.RED);
                    ct.fillOval(posX + 1, posY + 1, fieldWidth - 2, fieldHeight - 2);
                    posY += fieldHeight;
                }
                posX += fieldWidth;
            }
        });
    }

    @Override
    public void stop() {
        Platform.runLater(() -> startButton.setTitle("Start"));
    }

    @Override
    public void displayResult(ArrayList<Integer> scoreList, ArrayList<String> nameList) {
        Platform.runLater(() -> {
            Stage displayWinner = new Stage();
            displayWinner.initModality(Modality.APPLICATION_MODAL);
            VBox elements = new VBox();
            while (scoreList.size() > 0) {
                int score = Collections.max(scoreList);
                int index = scoreList.indexOf(score);
                elements.getChildren().add(new Label(nameList.get(index) + " - " + score));
                scoreList.remove(index);
                nameList.remove(index);
            }
            displayWinner.setTitle("Result");
            displayWinner.setScene(new Scene(elements));
            displayWinner.show();
        });
    }

    @Override
    public void setSpeed(double val) {
        g.setSpeed(val);
    }
}

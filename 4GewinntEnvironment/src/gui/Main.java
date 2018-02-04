package gui;

import control.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.GameField;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements Drawer, SpeedObject, MenuBarHandler {
    private Stage window;
    private Game g;
    private GameArea gameArea;
    private SpeedReader s;
    private PlayerView playerList;
    private ControlMenu menu;
    private Results resultScreen;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        g = new Game(this);
        resultScreen = new Results();
        menu = new ControlMenu(this);
        createMainGUI(window);
        selectClassFiles(null);
        primaryStage.show();
        g.requestRepaint();
    }

    private void createMainGUI(Stage window) {
        window.setTitle("4 in a Row - No game running!");
        window.setResizable(true);
        menu = new ControlMenu(this);
        BorderPane pane = new BorderPane();
        gameArea = new GameArea();
        gameArea.widthProperty().bind(pane.widthProperty());
        gameArea.heightProperty().bind(pane.heightProperty().subtract(menu.heightProperty()));
        gameArea.widthProperty().addListener((observable, oldValue, newValue) -> g.requestRepaint());
        gameArea.heightProperty().addListener((observable, oldValue, newValue) -> g.requestRepaint());
        pane.setTop(menu);
        pane.setCenter(gameArea);
        Scene mainScene = new Scene(pane, 400, 200);
        window.setOnCloseRequest(evt -> {
            if (s != null && s.isShown()) s.hide();
            if (playerList != null && playerList.isShown()) playerList.hide();
            g.stop();
            System.exit(0);
        });
        window.setScene(mainScene);
    }

    private void selectClassFiles(Stage w) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Enter AI-File!");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Java Classes", "*.class"));
        List<File> f;
        do {
            f = chooser.showOpenMultipleDialog(w);
            if (f != null) {
                for (File file : f) {
                    g.addClient(file);
                }
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
        Platform.runLater(() -> gameArea.draw(field));
    }

    @Override
    public void stop() {
        if (g.isRunning()) g.stop();
        Platform.runLater(() -> menu.getStart().setTitle("Start"));
    }


    @Override
    public void displayResult(ArrayList<Integer> scoreList, ArrayList<String> nameList) {
        Platform.runLater(() -> resultScreen.show(scoreList, nameList));
    }

    @Override
    public void setSpeed(double val) {
        g.setSpeed(val);
    }

    @Override
    public void startEvt(ActionEvent evt) {
        if (g.isRunning()) {
            menu.getStart().setTitle("Start");
            g.pause();
        } else {
            menu.getStart().setTitle("Pause");
            g.start();
        }
    }

    @Override
    public void stopEvt(ActionEvent evt) {
        if (g.isRunning())
            g.stop();
    }

    @Override
    public void slowEvt(ActionEvent evt) {
        if (g.isSlow()) {
            menu.getSlow().setTitle("Slow");
        } else {
            menu.getSlow().setTitle("Fast");
        }
        g.toggleMode();
    }

    @Override
    public void addEvt(ActionEvent evt) {
        selectClassFiles(window);
    }

    @Override
    public void speedEvt(ActionEvent evt) {
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
    }

    @Override
    public void listEvt(ActionEvent evt) {
        if (playerList == null) {
            playerList = new PlayerView(g.getPlayerHandler(), this);
            playerList.show();
        } else {
            if (playerList.isShown()) {
                playerList.hide();
            } else {
                playerList.show();
            }
        }
    }
}

package gui;

import control.PlayerHandler;
import control.Stoppable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.User;

import java.util.stream.Collectors;

public class PlayerView {
    private PlayerHandler source;
    private Stage s;
    private ListView<String> players;

    PlayerView(PlayerHandler source, Stoppable callback) {
        this.source = source;
        players = new ListView<>();
        Button remove = new Button("Remove");
        remove.setAlignment(Pos.CENTER);
        remove.autosize();
        remove.setOnAction(evt -> {
            callback.stop();
            players.getSelectionModel().getSelectedIndices().forEach(index -> {
                source.removePlayer(index);
                players.getItems().remove(index.intValue());
            });
        });
        VBox layout = new VBox(players, remove);
        layout.autosize();
        layout.setAlignment(Pos.CENTER);
        s = new Stage();
        s.setTitle("All Players");
        s.setScene(new Scene(layout));
        remove.prefWidthProperty().bind(s.widthProperty());
    }

    public void show() {
        players.getItems().clear();
        players.getItems().addAll(source.listPlayers().stream().map(User::getName).collect(Collectors.toList()));
        s.show();
    }

    public boolean isShown() {
        return s.isShowing();
    }

    public void hide() {
        s.hide();
    }
}

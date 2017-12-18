import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class SpeedReader {
    private SpeedObject reader;
    private Stage s;
    private Scene scene;
    private valWrapper value;

    public SpeedReader(SpeedObject obj) {
        reader = obj;
        value = new valWrapper();
        s = new Stage();
        Label val = new Label("1 move/s");
        Button b = new Button("Set");
        b.autosize();
        b.setOnAction(evt -> reader.setSpeed(value.val));
        Slider slider = new Slider(0.1, 10, 1);
        slider.autosize();
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            val.setText(new DecimalFormat("#.##").format(newValue.doubleValue()) + " move(s)/s");
            value.val = (Double) newValue;
        });
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        HBox controls = new HBox(val, b);
        controls.setAlignment(Pos.CENTER);
        VBox layout = new VBox(slider, controls);
        layout.setAlignment(Pos.CENTER);
        layout.autosize();
        scene = new Scene(layout, 200, 100);
        s.setTitle("Set Speed");
        s.setResizable(true);
        s.setScene(scene);
    }

    public void show() {
        s.show();
    }

    public boolean isShown() {
        return s.isShowing();
    }

    public void hide() {
        s.hide();
    }

    private class valWrapper {
        public double val = 1;
    }
}

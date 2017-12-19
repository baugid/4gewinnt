import javafx.geometry.Pos;
import javafx.scene.Scene;
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

    public SpeedReader(SpeedObject obj) {
        reader = obj;
        s = new Stage();
        Label val = new Label("1");
        Label txt = new Label(" move(s)/s");
        Slider slider = new Slider(0.1, 10, 1);
        slider.autosize();
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == slider.maxProperty().doubleValue())
                slider.maxProperty().setValue(slider.maxProperty().doubleValue() + 1);
            if (newValue.doubleValue() < slider.maxProperty().doubleValue() / 2.0 && slider.maxProperty().intValue() > 10)
                slider.maxProperty().setValue(slider.maxProperty().doubleValue() - 1);
            if (newValue.doubleValue() == slider.minProperty().doubleValue())
                slider.maxProperty().setValue(10.0);
            val.setText(new DecimalFormat("#.##").format(newValue.doubleValue()));
            reader.setSpeed(newValue.doubleValue());
        });
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        HBox controls = new HBox(val, txt);
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
}

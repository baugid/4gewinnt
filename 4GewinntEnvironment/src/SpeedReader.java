import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Locale;

public class SpeedReader {
    private SpeedObject reader;
    private Stage s;
    private Scene scene;
    private Double turnVal = -1D;

    public SpeedReader(SpeedObject obj) {
        reader = obj;
        s = new Stage();
        Label val = new Label("1");
        Label txt = new Label(" move(s)/s");
        Slider slider = new Slider(0.1, 10, 1);
        slider.autosize();
        //TODO Improve scaling
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() > oldValue.doubleValue()) {
                turnVal = -1D;
                slider.maxProperty().setValue(slider.maxProperty().doubleValue() + 0.2);
            }
            if (newValue.doubleValue() < oldValue.doubleValue() && slider.maxProperty().intValue() > 10) {
                if (turnVal == -1D) turnVal = oldValue.doubleValue();
                slider.maxProperty().setValue(slider.maxProperty().doubleValue() - ((oldValue.doubleValue() - newValue.doubleValue()) / ((turnVal - slider.minProperty().doubleValue()))) * (slider.maxProperty().doubleValue() - 10.0));
            }
            if (newValue.doubleValue() == slider.minProperty().doubleValue())
                slider.maxProperty().setValue(10.0);
            reader.setSpeed(newValue.doubleValue());
        });
        val.textProperty().bind(slider.valueProperty().asString(Locale.getDefault(), "%.2f"));
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

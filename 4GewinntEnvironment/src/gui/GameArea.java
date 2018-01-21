package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.GameField;

public class GameArea extends Canvas {
    public void draw(GameField field) {
        GraphicsContext ct = getGraphicsContext2D();
        ct.clearRect(0, 0, getWidth(), getHeight());
        double fieldWidth = getWidth() / field.field[0].length;
        double fieldHeight = (getHeight() /*- barHeight*/) / field.field.length;
        double posX = 0;
        double posY = 0;
        ct.setStroke(Color.BLACK);
        for (int i = 0; i < field.field.length - 1; i++) {
            posX += fieldWidth;
            ct.strokeLine(posX, 0, posX, getHeight());
        }
        for (int i = 0; i < field.field[0].length - 1; i++) {
            posY += fieldHeight;
            ct.strokeLine(0, posY, getWidth(), posY);
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
    }

    @Override
    public boolean isResizable() {
        return true;
    }
}

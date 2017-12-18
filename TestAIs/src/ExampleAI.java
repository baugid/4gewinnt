import utils.ChangeRequest;
import utils.GameField;
import utils.GameFieldHandle;
import utils.User;

import javax.swing.text.PlainDocument;
import java.util.Random;

import static utils.GameField.Value.NONE;
import static utils.GameField.Value.PLAYER1;

public class ExampleAI implements User {
    private GameField.Value player;
    private Random rand;

    public ExampleAI() {
        rand = new Random();
    }

    @Override

    public void init(GameField.Value player) {
        this.player = player;
    }

    @Override
    public ChangeRequest set(GameFieldHandle field) {
        int x = 0;
        int y = 0;
        while (field.getValue(x, y) != NONE) {
            x = rand.nextInt(field.getWidth());
            y = rand.nextInt(field.getHeight());
        }
        return new ChangeRequest(x, y, player);
    }

    @Override
    public String getName() {
        return "maxMusterCode";
    }
}

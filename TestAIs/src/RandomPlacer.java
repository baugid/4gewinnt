import utils.ChangeRequest;
import utils.GameField;
import utils.GameFieldHandle;
import utils.User;

import java.util.Random;

public class RandomPlacer implements User {
    private static int nr = 0;
    private Random r = new Random();
    private GameField.Value player;
    private int ownNr;

    public RandomPlacer() {
        ownNr = nr++;
    }

    @Override
    public void init(GameField.Value player) {
        this.player = player;
    }

    @Override
    public ChangeRequest set(GameFieldHandle field) {
        int x = r.nextInt(field.getWidth()), y = r.nextInt(field.getHeight());
        while (field.getValue(x, y) != GameField.Value.NONE) {
            x = r.nextInt(field.getWidth());
            y = r.nextInt(field.getHeight());
        }
        return new ChangeRequest(x, y, player);
    }

    @Override
    public String getName() {
        return "RandomPlacer" + ownNr;
    }
}

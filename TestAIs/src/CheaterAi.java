import utils.ChangeRequest;
import utils.GameField;
import utils.GameFieldHandle;
import utils.User;

import java.lang.reflect.Field;

public class CheaterAi implements User {
    private static int nr = 0;
    private int ownNr;
    private GameField.Value player;

    public CheaterAi() {
        ownNr = nr++;
    }

    @Override
    public void init(GameField.Value player) {
        this.player = player;
    }

    @Override
    public ChangeRequest set(GameFieldHandle field) {
        GameField f = null;
        try {
            Field gameAreaField = field.getClass().getDeclaredField("f");
            gameAreaField.setAccessible(true);
            f = (GameField) gameAreaField.get(field);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println("Unable to cheat!");
        }
        for (int i = 0; i < f.field.length; i++)
            for (int j = 0; j < f.field[i].length; j++)
                f.field[i][j] = player;
        f.field[0][0] = GameField.Value.NONE;
        return new ChangeRequest(0, 0, player);
    }

    @Override
    public String getName() {
        return "Cheater" + ownNr;
    }
}

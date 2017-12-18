import utils.ChangeRequest;
import utils.GameField;
import utils.GameFieldHandle;
import utils.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleSetter implements User {
    private static int nr = 0;
    private BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
    private GameField.Value player;
    private int ownNr;

    public ConsoleSetter() {
        ownNr = nr++;
    }

    @Override
    public void init(GameField.Value player) {
        this.player = player;
    }

    @Override
    public ChangeRequest set(GameFieldHandle field) {
        int x = 0, y = 0;
        System.out.print(ownNr + "x:");
        try {
            x = Integer.parseInt(consoleIn.readLine());

            System.out.print(ownNr + "y:");
            y = Integer.parseInt(consoleIn.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ChangeRequest(x, y, player);
    }

    @Override
    public String getName() {
        return "ConsoleIn" + ownNr;
    }
}

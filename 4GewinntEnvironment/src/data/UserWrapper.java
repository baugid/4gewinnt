package data;

import utils.ChangeRequest;
import utils.GameField;
import utils.GameFieldHandle;
import utils.User;

public class UserWrapper implements User {
    private static int userCount = 0;
    private User delegate;
    private int ownNumber;
    private boolean error = false;

    UserWrapper(User delegate) {
        this.delegate = delegate;
        ownNumber = userCount++;
    }

    @Override
    public void init(GameField.Value player) {
        error = false;
        try {
            delegate.init(player);
        } catch (Throwable e) {
            error = true;
            e.printStackTrace();
        }
    }

    @Override
    public ChangeRequest set(GameFieldHandle field) {
        if (!error)
            try {
                return delegate.set(field);
            } catch (Throwable e) {
                e.printStackTrace();
                return new ChangeRequest(-1, -1, GameField.Value.NONE);
            }
        else
            return new ChangeRequest(-1, -1, GameField.Value.NONE);
    }

    @Override
    public String getName() {
        try {
            return delegate.getName();
        } catch (Throwable e) {
            e.printStackTrace();
            return "User" + ownNumber;
        }
    }
}

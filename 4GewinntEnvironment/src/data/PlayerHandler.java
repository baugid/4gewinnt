package data;

import utils.User;

import java.io.File;
import java.util.List;

public interface PlayerHandler {
    boolean addPlayer(File f);

    boolean removePlayer(User u);

    boolean removePlayer(int index);

    List<User> listPlayers();
}

import utils.User;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class Players {
    private ArrayList<User> playerObjects;

    public Players() {
        playerObjects = new ArrayList<>();
    }

    public User getPlayer(int index) {
        return playerObjects.get(index);
    }

    public boolean loadPlayer(File file) {
        try {
            SecureClassLoader cl = new SecureClassLoader(new URL[]{file.getParentFile().toURI().toURL()});
            Class<?> user;
            String fileName = file.toPath().getFileName().toString();
            int pos = fileName.lastIndexOf(".");
            if (pos == -1) return false;
            user = cl.loadClass(fileName.substring(0, pos));
            if (!User.class.isAssignableFrom(user))
                return false;
            playerObjects.add(new UserWrapper((User) user.getConstructor().newInstance()));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public int getUserCount() {
        return playerObjects.size();
    }
}

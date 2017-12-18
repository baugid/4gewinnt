import utils.User;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class Players {
    private ArrayList<User> playerObjects;

    public Players() {
        playerObjects = new ArrayList<>();
    }

    public User getPlayer(int index) {
        return playerObjects.get(index);
    }

    public void loadPlayer(File file) {
        ClassLoader cl;
        try {
            cl = new URLClassLoader(new URL[]{file.getParentFile().toURI().toURL()});
        } catch (MalformedURLException e) {
            return;
        }
        Class<?> user;
        String fileName = file.toPath().getFileName().toString();
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) return;
        try {
            user = cl.loadClass(fileName.substring(0, pos));
        } catch (ClassNotFoundException e) {
            return;
        }
        if (!User.class.isAssignableFrom(user))
            return;
        try {
            playerObjects.add((User) user.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return;
        }
    }

    public int getUserCount() {
        return playerObjects.size();
    }
}

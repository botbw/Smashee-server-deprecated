package server.games.greedysnake;

import java.util.Locale;

public enum Direction {
    UP, // 0
    RIGHT, // 1
    DOWN,  // 2
    LEFT; // 3
    public static Direction getDir(String dir) {
        if(dir == null) return null;
        if(dir.toUpperCase().equals("UP")) {
            return Direction.UP;
        } else if(dir.toUpperCase().equals("RIGHT")) {
            return Direction.RIGHT;
        } else if(dir.toUpperCase().equals("DOWN")) {
            return Direction.DOWN;
        } else if(dir.toUpperCase().equals("LEFT")) {
            return Direction.LEFT;
        }
        return null;
    }
}

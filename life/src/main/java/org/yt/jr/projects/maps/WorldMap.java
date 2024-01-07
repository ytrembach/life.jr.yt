package org.yt.jr.projects.maps;

import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;
import java.util.Iterator;
import static org.yt.jr.projects.maps.LocationNeighbors.*;

public class WorldMap implements Iterable<Location> {
    final private int width;
    final private int height;
    final private Location[][] grid;

    private static WorldMap WorldMAP;

    public static WorldMap getMap() {
        if (WorldMAP == null) {
            WorldMAP = new WorldMap();
            WorldMAP.mapInit();
            Logger.Log(LogSources.SYSTEM, LogLevels.INFO, String.format("world map %dx%d created",
                    WorldMAP.width, WorldMAP.height));
        }
        return WorldMAP;
    }

    private WorldMap() {
        this.width = Config.CONFIG.getMapWidth();
        this.height = Config.CONFIG.getMapHeight();
        this.grid = new Location[width][height];
    }

    private void mapInit() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Location newLocation = new Location(row, col);
                grid[row][col] = newLocation;
                setNeighbors(row, col);
            }
        }
    }

    private void setNeighbors(int row, int col) {
        Location cell = grid[row][col];

        Location up = row > 0 ? grid[row - 1][col] : null;
        cell.setNeighbor(UP,up);
        if (up != null) {
            up.setNeighbor(DOWN,cell);
        }

        Location down = row < height - 1 ? grid[row + 1][col] : null;
        cell.setNeighbor(DOWN,down);
        if (down != null) {
            down.setNeighbor(UP,cell);
        }

        Location left = col > 0 ? grid[row][col - 1] : null;
        cell.setNeighbor(LEFT,left);
        if (left != null) {
            left.setNeighbor(RIGHT,cell);
        }

        Location right = col < width - 1 ? grid[row][col + 1] : null;
        cell.setNeighbor(RIGHT,right);
        if (right != null) {
            right.setNeighbor(LEFT,cell);
        }
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    public Location getLocation(int row, int col) {
        return grid[row][col];
    }

    @Override
    public Iterator<Location> iterator() {
        return new WorldMapIterator(this);
    }
}

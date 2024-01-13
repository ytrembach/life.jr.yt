package org.yt.jr.projects.maps;

import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;
import java.util.Iterator;
import static org.yt.jr.projects.maps.LocationNeighbors.*;

public class WorldMap implements Iterable<Location> {
    final private int cols;
    final private int rows;
    final private Location[][] grid;

    private static WorldMap WorldMAP;

    public static WorldMap getMap() {
        if (WorldMAP == null) {
            WorldMAP = new WorldMap();
            WorldMAP.mapInit();
            Logger.Log(LogSources.SYSTEM, LogLevels.INFO, String.format("world map %dx%d created",
                    WorldMAP.rows, WorldMAP.cols));
        }
        return WorldMAP;
    }

    private WorldMap() {
        this.cols = Config.CONFIG.mapCols();
        this.rows = Config.CONFIG.mapRows();
        this.grid = new Location[rows][cols];
    }

    private void mapInit() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
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

        Location down = row < rows - 1 ? grid[row + 1][col] : null;
        cell.setNeighbor(DOWN,down);
        if (down != null) {
            down.setNeighbor(UP,cell);
        }

        Location left = col > 0 ? grid[row][col - 1] : null;
        cell.setNeighbor(LEFT,left);
        if (left != null) {
            left.setNeighbor(RIGHT,cell);
        }

        Location right = col < cols - 1 ? grid[row][col + 1] : null;
        cell.setNeighbor(RIGHT,right);
        if (right != null) {
            right.setNeighbor(LEFT,cell);
        }
    }

    int getCols() {
        return cols;
    }

    int getRows() {
        return rows;
    }

    public Location getLocation(int row, int col) {
        return grid[row][col];
    }

    @Override
    public Iterator<Location> iterator() {
        return new WorldMapIterator(this);
    }
}

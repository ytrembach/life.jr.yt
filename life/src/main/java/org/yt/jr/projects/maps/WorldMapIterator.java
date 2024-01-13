package org.yt.jr.projects.maps;

import java.util.Iterator;

public class WorldMapIterator implements Iterator<Location> {

    final private WorldMap map;
    private int currRow;
    private int currCol;


    public WorldMapIterator(WorldMap map) {
        this.map = map;
        currRow = 0;
        currCol = 0;
    }

    @Override
    public boolean hasNext() {
        return currRow < map.getRows() - 1
                || currRow == map.getRows() - 1 && currCol < map.getCols();
    }

    @Override
    public Location next() {
        if (hasNext()) {
            Location next = map.getLocation(currRow, currCol);
            currCol++;
            if (currCol == map.getCols()) {
                currRow++;
                currCol = 0;
            }
            return next;
        } else {
            return null;
        }
    }
}

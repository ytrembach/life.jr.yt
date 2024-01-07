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
        return currRow == map.getWidth() ? false : true;
    }

    @Override
    public Location next() {
        if (hasNext()) {
            Location next = map.getLocation(currRow, currCol);
            if (currCol == map.getHeight() - 1) {
                currRow++;
                currCol = 0;
            } else {
                currCol++;
            }
            return next;
        } else {
            return null;
        }
    }
}

package org.yt.jr.projects.creatures;

import org.yt.jr.projects.maps.Location;

public abstract class Creature implements Runnable {
    private int health;
    private Location location;

    abstract void reproduce();

}

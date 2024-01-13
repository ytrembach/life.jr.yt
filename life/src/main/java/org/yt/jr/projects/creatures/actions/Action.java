package org.yt.jr.projects.creatures.actions;

import org.yt.jr.projects.creatures.Creature;

@FunctionalInterface
public interface Action {
    void doAction(final Creature party);
}

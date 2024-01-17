package org.yt.jr.projects.creatures.actions.move;

import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.maps.NeighborDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class LocationSelector {
    public static Optional<Location> getRandomDest(final Location origin, final int maxSteps) {
        if (maxSteps == 0) {
            return Optional.empty();
        }

        Stream<Location> candidates = Stream.of(origin);
        for (int steps = 1; steps <= maxSteps; steps++) {
            candidates = candidates
                    .flatMap(candidate ->
                    {
                        ArrayList<Location> withNeighbors = new ArrayList<>();
                        withNeighbors.add(candidate);
                        for (NeighborDirection direction : NeighborDirection.values()) {
                            Optional<Location> neighbor = candidate.getNeighbor(direction);
                            neighbor.ifPresent(withNeighbors::add);
                        }
                        return withNeighbors.stream();
                    })
                    .filter(location -> location.getId() != origin.getId())
                    .distinct();
        }
        final List<Location> result = candidates.toList();
        return Optional.of(result.get(ThreadLocalRandom.current().nextInt(0, result.size())));
    }
}

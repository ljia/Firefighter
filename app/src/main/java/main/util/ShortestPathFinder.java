/*
 * @auther  Allen Jia
 * @created 2021/09/03 7:44 PM
 */

package main.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ShortestPathFinder {
  public static int findShortestPath(Location origin, int numWalkers, List<Location> sitesToVisit) {
    if (numWalkers <= 0) {
      throw new IllegalArgumentException("Number of walkers must be greater than 0");
    }

    if (sitesToVisit == null || sitesToVisit.size() == 0) {
      return 0;
    }

    if (numWalkers > sitesToVisit.size()) {
      System.out.println("Restrict number of walkers to number of sites");
      numWalkers = sitesToVisit.size();
    }

    List<Location> origins = new ArrayList<>();
    for (int i = 0; i < numWalkers; i++) {
      origins.add(Location.of(origin.x, origin.y));
    }

    return findShortestPath(0, origins, sitesToVisit, new ArrayList<>());
  }

  private static int findShortestPath(
      int distanceSoFar,
      List<Location> currentLocations,
      List<Location> sitesToVisit,
      List<Location> sitesToSkip) {

    if (sitesToVisit.size() - sitesToSkip.size() == 1) {
      return distanceSoFar
          + calculateMinDistanceToOneSite(
              currentLocations,
              sitesToVisit.stream().filter(s -> !sitesToSkip.contains(s)).findFirst().get());
    }

    int shortestDistance = Integer.MAX_VALUE;
    int currentDistance;

    for (Location site : sitesToVisit) {
      if (sitesToSkip.contains(site)) {
        continue;
      }

      sitesToSkip.add(site);

      for (Location location : currentLocations) {
        Location locationBeforeMove = Location.of(location.x, location.y);
        location.moveTo(site);

        currentDistance =
            findShortestPath(
                distanceSoFar
                    + calculateMinDistanceBetweenTwoLocations(locationBeforeMove, location),
                currentLocations,
                sitesToVisit,
                sitesToSkip);

        shortestDistance = Math.min(shortestDistance, currentDistance);

        location.moveTo(locationBeforeMove);
      }

      sitesToSkip.remove(site);
    }

    return shortestDistance;
  }

  private static int calculateMinDistanceToOneSite(List<Location> locations, Location site) {
    int minDistance = Integer.MAX_VALUE;

    for (Location location : locations) {
      minDistance = Math.min(minDistance, calculateMinDistanceBetweenTwoLocations(location, site));
    }

    return minDistance;
  }

  private static int calculateMinDistanceBetweenTwoLocations(
      Location location1, Location location2) {
    return Math.abs(location1.x - location2.x) + Math.abs(location1.y - location2.y);
  }

  public static class Location {
    private int x;
    private int y;

    private Location(int x, int y) {
      // private constructor prevent direct creation
      this.x = x;
      this.y = y;
    }

    public static Location of(int x, int y) {
      return new Location(x, y);
    }

    public void moveTo(Location location) {
      this.x = location.x;
      this.y = location.y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Location location = (Location) o;
      return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Location.class.getSimpleName() + "[", "]")
          .add("x=" + x)
          .add("y=" + y)
          .toString();
    }
  }
}

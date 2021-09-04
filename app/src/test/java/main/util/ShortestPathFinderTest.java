/*
 * @auther  Allen Jia
 * @created 2021/09/03 8:08 PM
 */

package main.util;

import main.util.ShortestPathFinder.Location;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ShortestPathFinderTest {
  @Test
  public void testOneUser() {
    assertEquals(
        0,
        ShortestPathFinder.findShortestPath(
            Location.of(0, 0), 1, Collections.singletonList(Location.of(0, 0))));

    assertEquals(
        1,
        ShortestPathFinder.findShortestPath(
            Location.of(0, 0), 1, Collections.singletonList(Location.of(0, 1))));

    assertEquals(
        1,
        ShortestPathFinder.findShortestPath(
            Location.of(0, 0), 1, Collections.singletonList(Location.of(1, 0))));

    assertEquals(
        3,
        ShortestPathFinder.findShortestPath(
            Location.of(0, 0), 1, Arrays.asList(Location.of(1, 0), Location.of(0, 1))));

    assertEquals(
        2,
        ShortestPathFinder.findShortestPath(
            Location.of(0, 0), 2, Arrays.asList(Location.of(1, 0), Location.of(0, 1))));
  }
}

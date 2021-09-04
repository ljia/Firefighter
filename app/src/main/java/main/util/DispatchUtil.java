/*
 * Copyright (c) 2020 Learnta, Inc.
 * All right reserved.
 * @auther  Allen Jia
 * @created 2021/09/03 9:00 PM
 */

package main.util;

import main.api.Building;
import main.api.CityNode;
import main.api.Firefighter;
import main.api.exceptions.FireproofBuildingException;
import main.api.exceptions.NoFireFoundException;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DispatchUtil {
  public static List<Map.Entry<Firefighter, CityNode>> findShortestPath(
      List<Firefighter> firefighters, List<Building> burningSites) {
    if (firefighters == null || firefighters.size() == 0) {
      throw new IllegalArgumentException("There is no firefighter");
    }

    if (burningSites == null || burningSites.size() == 0) {
      return new ArrayList<>();
    }

    burningSites = burningSites.stream().filter(Building::isBurning).collect(Collectors.toList());

    if (burningSites.size() == 0) {
      return new ArrayList<>();
    }

    if (firefighters.size() > burningSites.size()) {
      System.out.println("Restrict number of firefighters to number of burningSites");
      firefighters = firefighters.subList(0, burningSites.size());
    }

    List<Map.Entry<Firefighter, CityNode>> moves = new ArrayList<>();
    findShortestPath(0, moves, firefighters, burningSites);
    return moves;
  }

  private static int findShortestPath(
      int distanceSoFar,
      List<Map.Entry<Firefighter, CityNode>> moves,
      List<Firefighter> firefighters,
      List<Building> sites) {

    List<Building> burningSites =
        sites.stream().filter(Building::isBurning).collect(Collectors.toList());

    if (burningSites.size() == 0) {
      return distanceSoFar;
    }

    int shortestDistance = Integer.MAX_VALUE;
    Firefighter bestFirefighter = null;
    Building bestSite = null;
    List<Map.Entry<Firefighter, CityNode>> bestMoves = null;
    int currentDistance;

    for (Building site : burningSites) {
      Firefighter firefighter = findClosestFirefighterToOneSite(firefighters, site);

      List<Map.Entry<Firefighter, CityNode>> newMoves = new ArrayList<>();

      CityNode locationBeforeMove =
          new CityNode(firefighter.getLocation().getX(), firefighter.getLocation().getY());

      firefighter.travelTo(site.getLocation());
      newMoves.add(new SimpleEntry<>(firefighter, site.getLocation()));

      try {
        site.extinguishFire();
      } catch (NoFireFoundException e) {
        // swallow since we know that it's safe
      }

      currentDistance =
          findShortestPath(
              distanceSoFar
                  + calculateDistanceBetweenTwoNodes(locationBeforeMove, firefighter.getLocation()),
              newMoves,
              firefighters,
              burningSites);

      if (currentDistance < shortestDistance) {
        bestFirefighter = firefighter;
        bestSite = site;
        shortestDistance = currentDistance;
        bestMoves = newMoves;
      }
      // FIXME: setFire() should not be used
      try {
        site.setFire();
      } catch (FireproofBuildingException e) {
        // swallow since we know that it's safe

      }

      firefighter.travelTo(locationBeforeMove);
    }

    //    try {
    //      bestFirefighter.travelTo(bestSite.getLocation(), false);
    //    } catch (NullPointerException e) {
    //      System.out.println("");
    //    }
    //    try {
    //      bestSite.extinguishFire();
    //    } catch (NoFireFoundException e) {
    //      // swallow since we know that it's safe
    //    }

    moves.addAll(bestMoves);

    return shortestDistance;
  }

  private static Firefighter findClosestFirefighterToOneSite(
      List<Firefighter> firefighters, Building burningSite) {
    int minDistance = Integer.MAX_VALUE;
    Firefighter closestFirefighter = null;

    for (Firefighter firefighter : firefighters) {
      int distance =
          calculateDistanceBetweenTwoNodes(firefighter.getLocation(), burningSite.getLocation());
      if (minDistance > distance) {
        minDistance = distance;
        closestFirefighter = firefighter;
      }
    }

    return closestFirefighter;
  }

  private static int calculateDistanceBetweenTwoNodes(CityNode node1, CityNode node2) {
    return Math.abs(node1.getX() - node2.getX()) + Math.abs(node1.getY() - node2.getY());
  }
}

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
    findShortestPath(0, moves, firefighters, burningSites, new ArrayList<>());

    return moves;
  }

  private static int findShortestPath(
      int distanceSoFar,
      List<Map.Entry<Firefighter, CityNode>> moves,
      List<Firefighter> firefighters,
      List<Building> sites,
      List<Building> sitesToSkip) {

    List<Building> sitesToVisit =
        sites.stream().filter(s -> !sitesToSkip.contains(s)).collect(Collectors.toList());

    if (sitesToVisit.size() == 0) {
      return distanceSoFar;
    }

    int shortestDistance = Integer.MAX_VALUE;
    List<Map.Entry<Firefighter, CityNode>> bestMoves = null;
    int currentDistance;

    for (Building site : sitesToVisit) {
      Firefighter firefighter = findClosestFirefighterToSite(firefighters, site);

      List<Map.Entry<Firefighter, CityNode>> newMoves = new ArrayList<>();

      CityNode locationBeforeMove =
          new CityNode(firefighter.getLocation().getX(), firefighter.getLocation().getY());

      firefighter.travelTo(site.getLocation());
      newMoves.add(new SimpleEntry<>(firefighter, site.getLocation()));

      sitesToSkip.add(site);

      currentDistance =
          findShortestPath(
              distanceSoFar
                  + calculateDistanceBetweenTwoNodes(locationBeforeMove, firefighter.getLocation()),
              newMoves,
              firefighters,
              sitesToVisit,
              sitesToSkip);

      if (currentDistance < shortestDistance) {
        shortestDistance = currentDistance;
        bestMoves = newMoves;
      }

      sitesToSkip.remove(site);

      firefighter.travelTo(locationBeforeMove);
    }

    moves.addAll(bestMoves);

    return shortestDistance;
  }

  private static Firefighter findClosestFirefighterToSite(
      List<Firefighter> firefighters, Building burningSite) {

    int minDistance = Integer.MAX_VALUE;
    Firefighter closestFirefighter = null;

    for (Firefighter firefighter : firefighters) {
      int distance =
          calculateDistanceBetweenTwoNodes(firefighter.getLocation(), burningSite.getLocation());
      if (distance < minDistance) {
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

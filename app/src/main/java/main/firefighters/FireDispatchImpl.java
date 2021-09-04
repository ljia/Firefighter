package main.firefighters;

import main.api.Building;
import main.api.City;
import main.api.CityNode;
import main.api.FireDispatch;
import main.api.Firefighter;
import main.api.exceptions.NoFireFoundException;
import main.util.DispatchUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FireDispatchImpl implements FireDispatch {
  private final City city;
  private final List<Firefighter> firefighters;

  public FireDispatchImpl(City city) {
    // TODO
    this.city = city;
    firefighters = new ArrayList<>();
  }

  @Override
  public void setFirefighters(int numFirefighters) {
    // TODO
    for (int i = 0; i < numFirefighters; i++) {
      firefighters.add(new FirefighterImpl(this.city.getFireStation().getLocation()));
    }
  }

  @Override
  public List<Firefighter> getFirefighters() {
    // TODO
    return this.firefighters;
  }

  @Override
  public void dispatchFirefighers(CityNode... burningBuildings) {
    // TODO
    if (burningBuildings == null) {
      burningBuildings = new CityNode[] {};
    }

    List<Building> buildings = new ArrayList<>();

    for (CityNode node : burningBuildings) {
      Building building = this.city.getBuilding(node);

      // Not clear in the assignment how to handle this: if a building is not burning, no need to
      // send a firefighter
      if (building.isBurning()) {
        buildings.add(building);
      }
    }

    List<Map.Entry<Firefighter, CityNode>> bestMoves =
        DispatchUtil.findShortestPath(this.firefighters, buildings);

    resetFirefighters();

    applyMoves(bestMoves);
  }

  private void resetFirefighters() {
    for (Firefighter firefighter : firefighters) {
      firefighter.travelTo(this.city.getFireStation().getLocation());
      firefighter.resetDistanceTraveled();
    }
  }

  private void applyMoves(List<Map.Entry<Firefighter, CityNode>> moves) {
    moves.forEach(
        m -> {
          m.getKey().travelTo(m.getValue());
          try {
            city.getBuilding(m.getValue()).extinguishFire();
          } catch (NoFireFoundException e) {
            e.printStackTrace();
          }
        });
  }
}

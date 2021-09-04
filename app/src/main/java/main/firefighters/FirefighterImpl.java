package main.firefighters;

import main.api.CityNode;
import main.api.Firefighter;

import java.util.AbstractMap;
import java.util.ArrayList;

public class FirefighterImpl implements Firefighter {
  private CityNode location;
  private int distanceTraveled;

  public FirefighterImpl(CityNode fireStation) {
    this.location = new CityNode(fireStation.getX(), fireStation.getY());
    this.distanceTraveled = 0;
  }

  @Override
  public CityNode getLocation() {
    // TODO
    return this.location;
  }

  @Override
  public int distanceTraveled() {
    // TODO
    return this.distanceTraveled;
  }

  @Override
  public void travelTo(CityNode destination) {
    this.distanceTraveled += calculateDistance(location, destination);
    this.location = destination;
  }

  @Override
  public void resetDistanceTraveled() {
    this.distanceTraveled = 0;
  }

  private int calculateDistance(CityNode src, CityNode dest) {
    return Math.abs(src.getX() - dest.getX()) + Math.abs(src.getY() - dest.getY());
  }
}

package main.api;

public interface Firefighter {

  /**
   * Get the firefighter's current location. Initially, the firefighter should be at the FireStation
   *
   * @return {@link CityNode} representing the firefighter's current location
   */
  CityNode getLocation();

  /**
   * Get the total distance traveled by this firefighter. Distances should be represented using
   * TaxiCab Geometry: https://en.wikipedia.org/wiki/Taxicab_geometry
   *
   * @return the total distance traveled by this firefighter
   */
  int distanceTraveled();

  /**
   * Move the firefighter to destination and calculate distanceTraveled
   *
   * @param destination the {@link CityNode} to travel to
   */
  void travelTo(CityNode destination);

  /**
   * Reset distanceTraveled to 0
   */
  void resetDistanceTraveled();
}

package main.firefighters;

import main.api.City;
import main.api.CityNode;
import main.api.FireDispatch;
import main.api.Firefighter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

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
    throw new NotImplementedException();
  }
}

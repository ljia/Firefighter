/*
 * Copyright (c) 2020 Learnta, Inc.
 * All right reserved.
 * @auther  Allen Jia
 * @created 2021/09/04 9:14 AM
 */

package main.scenarios;

import main.api.City;
import main.api.CityNode;
import main.api.FireDispatch;
import main.api.Firefighter;
import main.api.Pyromaniac;
import main.api.exceptions.FireproofBuildingException;
import main.impls.CityImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MoreScenarios {
  @Test
  public void noFirefighter() throws FireproofBuildingException {
    City basicCity = new CityImpl(10, 10, new CityNode(5, 0));
    FireDispatch fireDispatch = basicCity.getFireDispatch();

    CityNode[] fireNodes = {new CityNode(2, 0)};
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(0);

    try {
      fireDispatch.dispatchFirefighers(fireNodes);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      //
    }
  }

  @Test
  public void singleFirefighter() throws FireproofBuildingException {
    // No fire
    City basicCity = new CityImpl(10, 10, new CityNode(5, 0));
    FireDispatch fireDispatch = basicCity.getFireDispatch();

    CityNode[] fireNodes = {};
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(1);
    fireDispatch.dispatchFirefighers(fireNodes);

    Firefighter firefighter = fireDispatch.getFirefighters().get(0);
    Assert.assertEquals(0, firefighter.distanceTraveled());
    Assert.assertEquals(5, fireDispatch.getFirefighters().get(0).getLocation().getX());
    Assert.assertEquals(0, fireDispatch.getFirefighters().get(0).getLocation().getY());

    // False fire report
    basicCity = new CityImpl(10, 10, new CityNode(5, 0));
    fireDispatch = basicCity.getFireDispatch();

    fireNodes = new CityNode[] {new CityNode(2, 0), new CityNode(7, 0)};
    //    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(1);
    fireDispatch.dispatchFirefighers(fireNodes);

    firefighter = fireDispatch.getFirefighters().get(0);
    Assert.assertEquals(0, firefighter.distanceTraveled());
    Assert.assertEquals(5, fireDispatch.getFirefighters().get(0).getLocation().getX());
    Assert.assertEquals(0, fireDispatch.getFirefighters().get(0).getLocation().getY());

    // Two fires opposite direction
    basicCity = new CityImpl(10, 10, new CityNode(5, 0));
    fireDispatch = basicCity.getFireDispatch();

    fireNodes = new CityNode[] {new CityNode(2, 0), new CityNode(7, 0)};
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(1);
    fireDispatch.dispatchFirefighers(fireNodes);

    firefighter = fireDispatch.getFirefighters().get(0);
    Assert.assertEquals(7, firefighter.distanceTraveled());
    Assert.assertEquals(fireNodes[0], firefighter.getLocation());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());

    // Four fires four direction
    basicCity = new CityImpl(11, 11, new CityNode(5, 5));
    fireDispatch = basicCity.getFireDispatch();

    fireNodes =
        new CityNode[] {
          new CityNode(2, 5), new CityNode(7, 5), new CityNode(5, 10), new CityNode(5, 4)
        };
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(1);
    fireDispatch.dispatchFirefighers(fireNodes);

    firefighter = fireDispatch.getFirefighters().get(0);
    Assert.assertEquals(17, firefighter.distanceTraveled());
    Assert.assertEquals(fireNodes[2], firefighter.getLocation());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[2]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[3]).isBurning());

    // Four fires
    basicCity = new CityImpl(11, 11, new CityNode(5, 5));
    fireDispatch = basicCity.getFireDispatch();

    fireNodes =
        new CityNode[] {
          new CityNode(2, 0), new CityNode(7, 0), new CityNode(0, 10), new CityNode(0, 4)
        };
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(1);
    fireDispatch.dispatchFirefighers(fireNodes);

    firefighter = fireDispatch.getFirefighters().get(0);
    Assert.assertEquals(24, firefighter.distanceTraveled());
    Assert.assertEquals(fireNodes[2], firefighter.getLocation());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[2]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[3]).isBurning());
  }

  @Test
  public void testMultipleFirefighters() throws FireproofBuildingException {
    // Two firefighters two fire opposite direction
    City basicCity = new CityImpl(11, 11, new CityNode(5, 5));
    FireDispatch fireDispatch = basicCity.getFireDispatch();

    CityNode[] fireNodes = {new CityNode(2, 5), new CityNode(7, 5)};
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(2);
    fireDispatch.dispatchFirefighers(fireNodes);

    List<Firefighter> firefighters = fireDispatch.getFirefighters();
    int totalDistanceTraveled = 0;
    boolean firefighterPresentAtFireOne = false;
    boolean firefighterPresentAtFireTwo = false;
    for (Firefighter firefighter : firefighters) {
      totalDistanceTraveled += firefighter.distanceTraveled();

      if (firefighter.getLocation().equals(fireNodes[0])) {
        firefighterPresentAtFireOne = true;
      }
      if (firefighter.getLocation().equals(fireNodes[1])) {
        firefighterPresentAtFireTwo = true;
      }
    }

    Assert.assertEquals(5, totalDistanceTraveled);
    Assert.assertTrue(firefighterPresentAtFireOne);
    Assert.assertTrue(firefighterPresentAtFireTwo);
    Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());

    // Two firefighters 3 fires
    basicCity = new CityImpl(11, 11, new CityNode(5, 5));
    fireDispatch = basicCity.getFireDispatch();

    fireNodes = new CityNode[] {new CityNode(2, 5), new CityNode(7, 5), new CityNode(5, 10)};
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(2);
    fireDispatch.dispatchFirefighers(fireNodes);

    firefighters = fireDispatch.getFirefighters();
    totalDistanceTraveled = 0;
    firefighterPresentAtFireOne = false;
    firefighterPresentAtFireTwo = false;
    boolean firefighterPresentAtFireThree = false;
    for (Firefighter firefighter : firefighters) {
      totalDistanceTraveled += firefighter.distanceTraveled();

      if (firefighter.getLocation().equals(fireNodes[0])) {
        firefighterPresentAtFireOne = true;
      }
      if (firefighter.getLocation().equals(fireNodes[1])) {
        firefighterPresentAtFireTwo = true;
      }
      if (firefighter.getLocation().equals(fireNodes[2])) {
        firefighterPresentAtFireThree = true;
      }
    }

    Assert.assertEquals(12, totalDistanceTraveled);
    Assert.assertTrue(firefighterPresentAtFireOne);
    Assert.assertFalse(firefighterPresentAtFireTwo);
    Assert.assertTrue(firefighterPresentAtFireThree);
    Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());

    // Three firefighters 3 fires
    basicCity = new CityImpl(11, 11, new CityNode(5, 5));
    fireDispatch = basicCity.getFireDispatch();

    fireNodes = new CityNode[] {new CityNode(2, 5), new CityNode(7, 5), new CityNode(5, 10)};
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(3);
    fireDispatch.dispatchFirefighers(fireNodes);

    firefighters = fireDispatch.getFirefighters();
    totalDistanceTraveled = 0;
    firefighterPresentAtFireOne = false;
    firefighterPresentAtFireTwo = false;
    firefighterPresentAtFireThree = false;
    for (Firefighter firefighter : firefighters) {
      totalDistanceTraveled += firefighter.distanceTraveled();

      if (firefighter.getLocation().equals(fireNodes[0])) {
        firefighterPresentAtFireOne = true;
      }
      if (firefighter.getLocation().equals(fireNodes[1])) {
        firefighterPresentAtFireTwo = true;
      }
      if (firefighter.getLocation().equals(fireNodes[2])) {
        firefighterPresentAtFireThree = true;
      }
    }

    Assert.assertEquals(10, totalDistanceTraveled);
    Assert.assertTrue(firefighterPresentAtFireOne);
    Assert.assertTrue(firefighterPresentAtFireTwo);
    Assert.assertTrue(firefighterPresentAtFireThree);
    Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());

    // More firefighters than fire
    basicCity = new CityImpl(11, 11, new CityNode(5, 5));
    fireDispatch = basicCity.getFireDispatch();

    fireNodes = new CityNode[] {new CityNode(2, 5), new CityNode(7, 5), new CityNode(5, 10)};
    Pyromaniac.setFires(basicCity, fireNodes);

    fireDispatch.setFirefighters(5);
    fireDispatch.dispatchFirefighers(fireNodes);

    firefighters = fireDispatch.getFirefighters();
    totalDistanceTraveled = 0;
    firefighterPresentAtFireOne = false;
    firefighterPresentAtFireTwo = false;
    firefighterPresentAtFireThree = false;
    for (Firefighter firefighter : firefighters) {
      totalDistanceTraveled += firefighter.distanceTraveled();

      if (firefighter.getLocation().equals(fireNodes[0])) {
        firefighterPresentAtFireOne = true;
      }
      if (firefighter.getLocation().equals(fireNodes[1])) {
        firefighterPresentAtFireTwo = true;
      }
      if (firefighter.getLocation().equals(fireNodes[2])) {
        firefighterPresentAtFireThree = true;
      }
    }

    Assert.assertEquals(10, totalDistanceTraveled);
    Assert.assertTrue(firefighterPresentAtFireOne);
    Assert.assertTrue(firefighterPresentAtFireTwo);
    Assert.assertTrue(firefighterPresentAtFireThree);
    Assert.assertFalse(basicCity.getBuilding(fireNodes[0]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());
    Assert.assertFalse(basicCity.getBuilding(fireNodes[1]).isBurning());

    Assert.assertEquals(0, fireDispatch.getFirefighters().get(3).distanceTraveled());
    Assert.assertEquals(0, fireDispatch.getFirefighters().get(4).distanceTraveled());
  }
}

package edu.gatech.gtmaps;

import org.junit.Test;

import edu.gatech.gtmaps.models.Point;


import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import edu.gatech.gtmaps.models.Building;
import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.Hallway;
import edu.gatech.gtmaps.models.Junction;
import edu.gatech.gtmaps.models.Point;
import edu.gatech.gtmaps.models.Room;

import static org.junit.Assert.assertEquals;
/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class VectorUnitTest {
    @Test
    public void testIsLeftTurn() throws Exception {
        Point a = new Point(469.0f,83.0f);
        Point b = new Point(573.0f,83.0f);
        Point c = new Point(580.0f,83.0f);
        Point d = new Point(581.0f,205.0f);
        SearchObject test = new SearchObject();
        boolean res = test.isLeftTurn(a,b,c,d);
        assertEquals(res, true);
        System.out.println("isLeftTurn: "+res);
    }
}
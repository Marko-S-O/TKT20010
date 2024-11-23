package com.orasaari;

import java.awt.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Unit test for...
 */
public class DijkstraPathfinderTest {

    private DijkstraPathfinder pathfinder;

    @BeforeEach
    public void setUp() {
        System.out.println("Starting test...");
        pathfinder = new DijkstraPathfinder();

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Test finished.");
        pathfinder = null;
    }

    /* 
     * Test that the pathfinder finds the correct path in an empty map.
    */
    @Test
    public void shouldFindCorrectPathInEmptyMap() {
        GridMap map = new GridMap(1024, 1024);
        map.randomize(0.0);
        Point start = new Point(5, 5);
        Point goal = new Point(1000, 500);
        Result result = pathfinder.navigate(map, start, goal);
        assertEquals(result.numOfPathNodes, 996);
        assertEquals(result.distance, 1200.04, 0.01);
    }
 
    /* 
     * Test that the pathfinder finds the correct path in a city map.
     * This is the longest scenario for Berlin_0_256.map.
    */
    @Test
    public void shouldFindCorrectPathInCityMap() {
        GridMap map = MapUtil.loadMap(TestUtils.TEST_DATA_DIRECTORY + "Berlin_0_256.map");
        Point start = new Point(9    , 25);
        Point goal = new Point(245, 251);
        Result result = pathfinder.navigate(map, start, goal, false);
        assertEquals(result.distance, 369.45, 0.01);
    }
}

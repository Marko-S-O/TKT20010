package com.orasaari;

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
        pathfinder = new DijkstraPathfinder();

    }

    @AfterEach
    public void tearDown() {
        pathfinder = null;
    }

    /* 
     * Test scenarios with main moving direction to all 8 directions.
    */
    @Test
    public void shouldFindCorrectPathToAllDirections() {
        assertTrue(TestUtils.testInCityMap(pathfinder, "Berlin_2_256.map", 2, 249, 253, 16, 370.94321747)); // case 1.1
        assertTrue(TestUtils.testInCityMap(pathfinder, "NewYork_1_256.map", 9, 123, 240, 117, 250.05382385)); // case 1.2
        assertTrue(TestUtils.testInCityMap(pathfinder, "Sydney_1_256.map", 1, 1, 216, 234, 340.80108185)); // case 1.3
        assertTrue(TestUtils.testInCityMap(pathfinder, "London_1_256.map", 133, 12, 129, 248, 262.12489166)); // case 1.4
        assertTrue(TestUtils.testInCityMap(pathfinder, "Moscow_0_256.map", 247, 4, 7, 247, 361.05591583)); // case 1.5
        assertTrue(TestUtils.testInCityMap(pathfinder, "Paris_2_256.map", 253, 128, 4, 125, 275.09545441)); // case 1.6
        assertTrue(TestUtils.testInCityMap(pathfinder, "Boston_1_256.map", 249, 254, 4, 9, 363.26911926)); // case 1.7
        assertTrue(TestUtils.testInCityMap(pathfinder, "Denver_1_256.map", 120, 254, 118, 2, 261.94112549)); // case 1.8
    }

    /* 
     * Test starting points in different edges of the map
    */
    @Test
    public void shouldHandleAllStartingPointsCorrectly() {        
        assertTrue(TestUtils.testInCityMap(pathfinder, "Milan_0_256.map", 255, 0, 176, 66, 106.33809509)); // case 2.1
        assertTrue(TestUtils.testInCityMap(pathfinder, "Berlin_0_256.map", 255, 17, 253, 108, 95.97056274)); // case 2.2
        assertTrue(TestUtils.testInCityMap(pathfinder, "Berlin_0_256.map", 255, 255, 50, 26, 336.17366485)); // case 2.3
        assertTrue(TestUtils.testInCityMap(pathfinder, "Berlin_0_256.map", 125, 255, 47, 181, 157.3969696)); // case 2.4
        assertTrue(TestUtils.testInCityMap(pathfinder, "Denver_0_256.map", 0, 255, 254, 102, 340.22034607)); // case 2.5
        assertTrue(TestUtils.testInCityMap(pathfinder, "Berlin_0_256.map", 0, 186, 88, 232, 239.22034607)); // case 2.6
        assertTrue(TestUtils.testInCityMap(pathfinder, "Denver_0_256.map", 0, 0, 221, 195, 316.41630554)); // case 2.7
        assertTrue(TestUtils.testInCityMap(pathfinder, "Boston_0_256.map", 1, 0, 17, 138, 190.30865784)); // case 2.8
    }

    /* 
     * Test goals in the different edges of the map
    */
    @Test
    public void shouldHandleAllGoalsCorrectly() {
        Pathfinder pathfinder = new AStarPathfinder();
        assertTrue(TestUtils.testInCityMap(pathfinder, "Moscow_0_256.map", 10, 188, 255, 0, 324.04372253)); // case 3.1
        assertTrue(TestUtils.testInCityMap(pathfinder, "Denver_1_256.map", 231, 115, 255, 111, 25.65685425)); // case 3.2
        assertTrue(TestUtils.testInCityMap(pathfinder, "Shanghai_0_256.map", 131, 20, 255, 255, 325.91883087)); // case 3.3
        assertTrue(TestUtils.testInCityMap(pathfinder, "London_1_256.map", 224, 240, 240, 255, 22.21320343)); // case 3.4
        assertTrue(TestUtils.testInCityMap(pathfinder, "Moscow_2_256.map", 243, 23, 0, 255, 356.08535309)); // case 3.5
        assertTrue(TestUtils.testInCityMap(pathfinder, "Boston_2_256.map", 104, 174, 0, 188, 109.79898987)); // case 3.6
        assertTrue(TestUtils.testInCityMap(pathfinder, "Paris_2_256.map", 31, 157, 0, 0, 396.27416992)); // case 3.7
        assertTrue(TestUtils.testInCityMap(pathfinder, "NewYork_0_256.map", 70, 87, 52, 0, 94.45584412)); // case 3.8
    }

    /* 
     * Test special cases: pathfinding in an empty map, adjacent nodes, same start and goal points.
    */
    @Test
    public void shouldHandleSpecialCasesCorrectly() {

        // create an empty map without obstacles
        GridMap map = new GridMap(1024, 1024, 0.0);

        assertTrue(TestUtils.testInOtherMap(pathfinder, map, 5, 5, 6, 5, 1)); // case 4.1
        assertTrue(TestUtils.testInOtherMap(pathfinder, map, 5, 5, 5, 6, 1)); // case 4.2
        assertTrue(TestUtils.testInOtherMap(pathfinder, map, 5, 5, 6, 6, MapUtils.SQRT2)); // case 4.3
        assertTrue(TestUtils.testInOtherMap(pathfinder, map, 5, 5, 5, 5, 0)); // case 4.4
        assertTrue(TestUtils.testInOtherMap(pathfinder, map, 5, 5, 1000, 500, 1200.04)); // case 4.5
    }
    
}

package com.orasaari;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MapUtilTest {

    private static final String MAP_FILE = "Berlin_0_256.map";
    
    @BeforeEach
    public void setUp() {
        System.out.println("Starting test...");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Test finished.");
    }

    private boolean validateMap(GridMap map) {
        int pixels = 0;
        int obstacles = 0;
        boolean[][] grid = map.getGrid();
        for(int i=0; i<map.getWidth(); i++) {
            for(int j=0; j<map.getHeight(); j++) {
                pixels++;
                if(!grid[i][j]) {
                    obstacles++;
                }
            }
        }   
        System.out.println(obstacles);
        boolean ok = map.getWidth() == 256 && map.getHeight() == 256 && pixels == 65536 && obstacles == 17389;
        return ok;
    }

    @Test
    public void resultsShouldBeCollectedCorrenctly() {
        List<Node> path = TestUtils.getTestPath();
        Node finishNodel = path.get(path.size()-1);
        long startTime = System.currentTimeMillis();
        Result result = MapUtils.collectResults(finishNodel, startTime, startTime+1000, 5000, MapUtils.ALGORITHM_ASTAR, true);
        assertEquals(result.numOfPathNodes, 996);
        assertEquals(result.distance, 1200.04, 0.01);
        assertEquals(result.duration, 1000);
    }


    @Test
    public void mapShouldLoadCorrenctly() {
        GridMap map = MapUtils.loadMap(TestUtils.TEST_DATA_DIRECTORY + MAP_FILE);
        assertTrue(validateMap(map));
    }

    @Test
    public void octileDistanceShouldBeCalculatedCorrenctly() {
        double distance = MapUtils.octileDistance(5,5, 1000,500);
        assertEquals(distance, 1200.04, 0.01);
    }


}
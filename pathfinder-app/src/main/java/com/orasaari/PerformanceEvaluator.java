package com.orasaari;

public class PerformanceEvaluator {
    
    private static class Scenario {
        int id;
        String map;
        int startX;
        int startY;
        int goalX;
        int goalY;
        double distance;

        Scenario(int id, String map, int startX, int startY, int goalX, int goalY, double distance) {
            this.id = id;
            this.map = map;
            this.startX = startX;
            this.startY = startY;
            this.goalX = goalX;
            this.goalY = goalY;
            this.distance = distance;
        }   
    }

    private static class Evaluation {
        
    }

    private Scenario[] scenarios = {
        new Scenario(1, " ", 1, 2, 3, 4, 5.0),
        new Scenario(2, " ", 1, 2, 3, 4, 5.0)    
    };

    PerformanceEvaluator() {
        System.out.println("PerformanceEvaluator started.");

    }   

    public void runEvaluation(int iterations) {

    }



    public static void main(String[] args) {
        PerformanceEvaluator p = new PerformanceEvaluator();
        p.runEvaluation(1000);
    }   
    
}

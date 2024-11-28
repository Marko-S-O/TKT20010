package com.orasaari;

import java.text.SimpleDateFormat;

/* 
* A wrapper class for results of a single evaluation: one scenario, one algorithm, one iteration.
*/
class PerformanceEvaluation {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    int algorithm;
    PathfindingResult result;
    long startTime;
    long finishTime;    
    boolean correctDistance;

    /**
     * Construct a line for a CSV export file.
     * 
     * @return  CSV representation of the evaluation
     */
    String toCsvString() {
        return(
            TIMESTAMP_FORMAT.format(result.startTime) + "," +
            result.seachDuration + "," + 
            result.algorithmCode + "," + 
            result.distance + "," + 
            result.goalFound + "," + 
            correctDistance + "," +
            result.numberOfPathNodes + "," + 
            result.nodesEvaluated
        );    
    }
}

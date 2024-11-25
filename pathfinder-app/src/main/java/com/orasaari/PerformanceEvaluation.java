package com.orasaari;

import java.text.SimpleDateFormat;

/* 
    * A wrapper class for results of a single evaluation: one scenario, one algorithm, one iteration.
    */
class PerformanceEvaluation {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    int algorithm;
    Result result;
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
            result.duration + "," + 
            result.algorithm + "," + 
            result.distance + "," + 
            result.success + "," + 
            correctDistance + "," +
            result.numOfPathNodes + "," + 
            result.numeOfEvaluatedNodes
        );    
    }
}

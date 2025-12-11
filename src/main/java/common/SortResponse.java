package common;

import java.io.Serial;
import java.io.Serializable;

public class SortResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int[] sortedData;
    private long executionTimeNano;

    public SortResponse(int[] sortedData, long executionTimeNano) {
        this.sortedData = sortedData;
        this.executionTimeNano = executionTimeNano;
    }

    public int[] getSortedData() { return sortedData; }
    public long getExecutionTimeNano() { return executionTimeNano; }
}
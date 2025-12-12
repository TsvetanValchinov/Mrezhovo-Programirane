package common;

import java.io.Serializable;

public record SortResponse(int[] sortedData, long executionTimeNano) implements Serializable {
    private static final long serialVersionUID = 1L;
}
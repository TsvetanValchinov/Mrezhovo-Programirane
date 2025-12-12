package common;

import java.io.Serializable;

public record SortRequest(int[] data, int threadCount) implements Serializable {

    private static final long serialVersionUID = 1L;
}

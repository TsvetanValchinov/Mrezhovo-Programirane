package common;

import java.io.Serial;
import java.io.Serializable;

public class SortRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int[] data;
    private int threadCount;

    public SortRequest(int[] data, int threadCount) {
        this.data = data;
        this.threadCount = threadCount;
    }

    public int[] getData() { return data; }
    public int getThreadCount() { return threadCount; }
}

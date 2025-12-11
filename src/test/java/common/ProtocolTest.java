package common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocolTest {

    @Test
    void testSortRequestCreation() {
        int[] data = {1, 2, 3};
        int threads = 4;

        SortRequest request = new SortRequest(data, threads);

        assertNotNull(request);
        assertArrayEquals(data, request.getData());
        assertEquals(threads, request.getThreadCount());
    }

    @Test
    void testSortResponseCreation() {
        int[] sortedData = {1, 2, 3};
        long time = 123456789L;

        SortResponse response = new SortResponse(sortedData, time);

        assertNotNull(response);
        assertArrayEquals(sortedData, response.getSortedData());
        assertEquals(time, response.getExecutionTimeNano());
    }

    @Test
    void testDataIntegrity() {
        int[] original = {10, 20, 30};
        SortRequest request = new SortRequest(original, 2);

        assertEquals(10, request.getData()[0]);
    }
}
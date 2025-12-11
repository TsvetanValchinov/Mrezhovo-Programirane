package server;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ParallelMergeSorterTest {

    @Test
    void testStandardSort() {
        int[] input = {5, 1, 9, 3, 7, 6, 2, 8, 4, 0};
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        ForkJoinPool pool = new ForkJoinPool(4);
        ParallelMergeSorter sorter = new ParallelMergeSorter(input, 0, input.length - 1);
        pool.invoke(sorter);

        assertArrayEquals(expected, input, "Масивът не е сортиран правилно!");
    }

    @Test
    void testLargeRandomArray() {
        int size = 100_000;
        int[] actual = generateRandomArray(size);
        int[] expected = actual.clone();

        Arrays.sort(expected);

        ForkJoinPool pool = new ForkJoinPool(8);
        ParallelMergeSorter sorter = new ParallelMergeSorter(actual, 0, actual.length - 1);
        pool.invoke(sorter);

        assertArrayEquals(expected, actual, "Големият масив не съвпада със стандартния sort!");
    }

    @Test
    void testEmptyArray() {
        int[] input = {};
        int[] expected = {};

        ForkJoinPool pool = new ForkJoinPool(2);
        ParallelMergeSorter sorter = new ParallelMergeSorter(input, 0, input.length - 1);
        pool.invoke(sorter);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSingleElementArray() {
        int[] input = {42};
        int[] expected = {42};

        ForkJoinPool pool = new ForkJoinPool(2);
        ParallelMergeSorter sorter = new ParallelMergeSorter(input, 0, input.length - 1);
        pool.invoke(sorter);

        assertArrayEquals(expected, input);
    }

    private int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt();
        }
        return arr;
    }
}
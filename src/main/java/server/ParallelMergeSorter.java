package server;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSorter extends RecursiveAction {
    private final int[] array;
    private final int left;
    private final int right;

    private static final int THRESHOLD = 100;

    public ParallelMergeSorter(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (right - left < THRESHOLD) {
            Arrays.sort(array, left, right + 1);
        } else {
            int mid = (left + right) / 2;

            ParallelMergeSorter leftTask = new ParallelMergeSorter(array, left, mid);
            ParallelMergeSorter rightTask = new ParallelMergeSorter(array, mid + 1, right);

            invokeAll(leftTask, rightTask);

            merge(mid);
        }
    }

    private void merge(int mid) {
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) temp[k++] = array[i++];
        while (j <= right) temp[k++] = array[j++];

        System.arraycopy(temp, 0, array, left, temp.length);
    }
}
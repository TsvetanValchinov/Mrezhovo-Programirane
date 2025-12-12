package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import common.SortRequest;
import common.SortResponse;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 5555;
    private static final int ARRAY_SIZE = 1_000_000;

    public static void main(String[] args) {
        long seed = 42L;
        int[] data = generateRandomArray(ARRAY_SIZE, seed);

        System.out.println("--- ТЕСТ СРАВНЕНИЕ (Array Size: " + ARRAY_SIZE + ") ---");

        try (Socket socket = new Socket(SERVER_IP, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            long timeSingle = sendRequest(out, in, data.clone(), 1);
            System.out.println("Време (1 нишка): " + timeSingle / 1_000_000 + " ms");

            int threads = Runtime.getRuntime().availableProcessors();
            long timeMulti = sendRequest(out, in, data.clone(), threads);
            System.out.println("Време (" + threads + " нишки): " + timeMulti / 1_000_000 + " ms");

            if (timeMulti < timeSingle) {
                double speedup = (double) timeSingle / timeMulti;
                System.out.printf("Паралелното изпълнение е %.2f пъти по-бързо.\n", speedup);
            } else {
                System.out.println("Единичната нишка беше по-бърза.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long sendRequest(ObjectOutputStream out, ObjectInputStream in, int[] data, int threads) throws Exception {
        SortRequest request = new SortRequest(data, threads);
        out.writeObject(request);
        out.flush();
        out.reset();

        SortResponse response = (SortResponse) in.readObject();
        return response.executionTimeNano();
    }


    private static int[] generateRandomArray(int size, long seed) {
        Random random = new Random(seed);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(10000);
        }
        return arr;
    }
}
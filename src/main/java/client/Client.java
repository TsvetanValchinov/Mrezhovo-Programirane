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
        int[] data = generateRandomArray(ARRAY_SIZE);

        System.out.println("--- ТЕСТ СРАВНЕНИЕ (Array Size: " + ARRAY_SIZE + ") ---");

        long timeSingle = sendRequest(data.clone(), 1);
        System.out.println("Време (1 нишка): " + timeSingle / 1_000_000 + " ms");

        int threads = Runtime.getRuntime().availableProcessors();
        long timeMulti = sendRequest(data.clone(), threads);
        System.out.println("Време (" + threads + " нишки): " + timeMulti / 1_000_000 + " ms");

        if (timeMulti < timeSingle) {
            double speedup = (double) timeSingle / timeMulti;
            System.out.printf("Паралелното изпълнение е %.2f пъти по-бързо.\n", speedup);
        } else {
            System.out.println("Единичната нишка беше по-бърза.");
        }
    }

    private static long sendRequest(int[] data, int threads) {
        try (Socket socket = new Socket(SERVER_IP, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            SortRequest request = new SortRequest(data, threads);
            out.writeObject(request);

            SortResponse response = (SortResponse) in.readObject();
            return response.getExecutionTimeNano();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(10000);
        }
        return arr;
    }
}
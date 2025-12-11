package server;

import common.SortRequest;
import common.SortResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            SortRequest request = (SortRequest) in.readObject();
            int[] data = request.getData();
            int threads = request.getThreadCount();

            System.out.println("Получена заявка: " + data.length + " елемента, " + threads + " нишки.");

            ForkJoinPool pool = new ForkJoinPool(threads);
            ParallelMergeSorter sorter = new ParallelMergeSorter(data, 0, data.length - 1);

            long startTime = System.nanoTime();
            pool.invoke(sorter);
            long endTime = System.nanoTime();

            SortResponse response = new SortResponse(data, (endTime - startTime));
            out.writeObject(response);

            System.out.println("Обработката завърши за: " + (endTime - startTime) + " ns");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
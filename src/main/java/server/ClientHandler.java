package server;

import common.SortRequest;
import common.SortResponse;

import java.io.EOFException;
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
            while (true) {
                try {
                    SortRequest request = (SortRequest) in.readObject();
                    int[] data = request.data();
                    int requestedThreads = request.threadCount();

                    System.out.println("Получена заявка: " + data.length + " елемента, " + requestedThreads + " нишки.");

                    int effectiveThreads = requestedThreads;
                    int availableThreads = Runtime.getRuntime().availableProcessors();

                    effectiveThreads = data.length < ParallelMergeSorter.THRESHOLD ? 1 : requestedThreads;
                    effectiveThreads = Math.min(effectiveThreads, availableThreads);

                    System.out.println("Start sorting: Size=" + data.length + ", Threads=" + effectiveThreads);

                    ForkJoinPool pool = new ForkJoinPool(effectiveThreads);
                    ParallelMergeSorter sorter = new ParallelMergeSorter(data, 0, data.length - 1);

                    long startTime = System.nanoTime();
                    pool.invoke(sorter);
                    long endTime = System.nanoTime();

                    System.out.println("Обработката завърши за: " + (endTime - startTime) + " ns");

                    SortResponse response = new SortResponse(data, (endTime - startTime));
                    out.writeObject(response);
                    out.flush();
                    out.reset();
                }
                catch (EOFException e) {
                    System.out.println("Клиентът прекъсна връзката.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
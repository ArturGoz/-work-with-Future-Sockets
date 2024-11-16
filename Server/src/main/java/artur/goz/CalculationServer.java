package artur.goz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


//Реалізуйте:  блокуючі запити, Future, звичайний сокет (java.net.Socket), Java

public class CalculationServer {
    private static final int PORT = 8080;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new CalculationTask(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class CalculationTask implements Runnable {
        private Socket socket;

        public CalculationTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String input;
                while ((input = in.readLine()) != null) {
                    if ("status".equalsIgnoreCase(input)) {
                        out.println("Status: Ready for calculation.");
                    } else {
                        int x = Integer.parseInt(input);
                        Future<Double> futureResult = executorService.submit(() -> functionG(x));
                        out.println("Result: " + futureResult.get());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private double functionG(int x) {
            return Math.pow(x, 2) + 3.0;
        }
    }
}

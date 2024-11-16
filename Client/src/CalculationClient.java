
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class CalculationClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server. Type 'status' to check status or enter an integer to calculate.");

            while (true) {
                System.out.print("Enter command: ");
                String input = scanner.nextLine();
                out.println(input);
                String response = in.readLine();
                System.out.println("Server response: " + response);

                if ("exit".equalsIgnoreCase(input)) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

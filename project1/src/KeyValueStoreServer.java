import java.io.*;
import java.net.*;
import java.util.*;

public class KeyValueStoreServer {
    private static HashMap<String, String> store = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("Server started on port 8000...");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String input = in.readLine();
                    String[] parts = input.split(" ");
                    String operation = parts[0];
                    String key = parts[1];

                    if (operation.equalsIgnoreCase("PUT")) {
                        String value = parts[2];
                        store.put(key, value);
                        out.println("PUT Successful");
                    } else if (operation.equalsIgnoreCase("GET")) {
                        String value = store.get(key);
                        out.println(value);
                    } else if (operation.equalsIgnoreCase("DELETE")) {
                        store.remove(key);
                        out.println("DELETE Successful");
                    } else {
                        out.println("Invalid Operation");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            System.out.print(store);
        }
    }
}

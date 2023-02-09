import java.io.*;
import java.net.*;
import java.util.*;

public class KeyValueStoreServerTCP {
    private static HashMap<String, String> store = new HashMap<>();

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out
                .println(InetAddress.getLocalHost().getHostAddress() + "Server started on port " + portNumber + "...");

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

                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout exception: " + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            System.out.print(store);
        }
    }
}

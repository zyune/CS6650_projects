import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class KeyValueStoreServerUDP {
    private static HashMap<String, String> store = new HashMap<>();

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        DatagramSocket socket = new DatagramSocket(portNumber);
        System.out.println("the server is running on 127.0.0.1 port " + portNumber);
        byte[] buffer = new byte[1024];

        while (true) {
            try {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.setSoTimeout(10000);
                socket.receive(request);
                String message = new String(request.getData(), 0, request.getLength());
                String[] parts = message.split(" ");
                String operation = parts[0];
                String key = parts[1];
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();
                String response = "";
                if (operation.equalsIgnoreCase("PUT")) {
                    String value = parts[2];
                    store.put(key, value);
                    response = "Value stored successfully";
                } else if (operation.equalsIgnoreCase("GET")) {
                    String value = store.get(key);
                    if (value != null) {
                        response = "Value for key " + key + ": " + value;
                    } else {
                        response = "Key not found";
                    }
                } else if (operation.equalsIgnoreCase("DELETE")) {
                    store.remove(key);
                    response = "Key deleted successfully";
                } else {
                    response = "Invalid operation";
                }
                byte[] responseBytes = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, clientAddress,
                        clientPort);
                socket.send(responsePacket);

            } catch (SocketTimeoutException e) {
                System.out.println("Timeout exception: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

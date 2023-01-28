import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class KeyValueStoreServerUDP {
    private static HashMap<String, String> store = new HashMap<>();

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9876);
        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
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
        }
    }
}

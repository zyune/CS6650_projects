
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class KeyValueStoreClientUDP {
    public static void main(String[] args) throws IOException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        Scanner in = new Scanner(System.in);
        InetAddress address = InetAddress.getByName(hostName);
        DatagramSocket socket = new DatagramSocket();
        while (true) {
            try {
                System.out.print("Enter operation (PUT, GET, DELETE, or exit): ");
                String operation = in.nextLine();
                if (operation.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.print("Enter key: ");
                String key = in.nextLine();
                String message = operation + " " + key;
                if (operation.equalsIgnoreCase("PUT")) {
                    System.out.print("Enter value: ");
                    String value = in.nextLine();
                    message += " " + value;
                }
                byte[] messageBytes = message.getBytes();
                DatagramPacket request = new DatagramPacket(messageBytes, messageBytes.length, address, portNumber);
                socket.send(request);

                byte[] buffer = new byte[1024];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);
                String responseMessage = new String(response.getData(), 0, response.getLength());
                System.out.println(responseMessage);
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout exception: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

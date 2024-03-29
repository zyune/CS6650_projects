import java.io.*;
import java.net.*;
import java.util.*;

public class KeyValueStoreClientTCP {

    public static void main(String[] args) throws IOException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        while (true) {
            try (
                    Socket socket = new Socket(hostName, portNumber);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter operation (PUT, GET, DELETE) or 'exit' to close the connection : ");
                String operation = scanner.nextLine();
                if (operation.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.print("Enter key: ");
                String key = scanner.nextLine();

                String value = "";
                if (operation.equalsIgnoreCase("PUT")) {
                    System.out.print("Enter value: ");
                    value = scanner.nextLine();
                }

                String message = operation + " " + key + " " + value;
                out.println(message);

                String response = in.readLine();
                System.out.println(response);
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout exception: " + e.getMessage());
            } catch (UnknownHostException e) {
                System.err.println("Unknown host ");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to ");
                System.exit(1);
            }
        }

    }
}

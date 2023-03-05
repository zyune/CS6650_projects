
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            // assign port number to portNumber
            int portNumber = Integer.parseInt(args[0]);
            // Look up the RMI service implementation in the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", portNumber);
            KeyValueService service = (KeyValueService) registry.lookup("KeyValueService");

            // Create a scanner object to read user input
            Scanner scanner = new Scanner(System.in);

            // Loop to read user input and perform operations on the key-value store
            while (true) {
                System.out.print("Enter operation (put, get, delete) or 'quit' to exit: ");
                String operation = scanner.nextLine();

                if (operation.equalsIgnoreCase("quit")) {
                    break;
                } else if (operation.equalsIgnoreCase("put")) {
                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();
                    System.out.print("Enter value: ");
                    String value = scanner.nextLine();
                    service.put(key, value);
                } else if (operation.equalsIgnoreCase("get")) {
                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();
                    String value = service.get(key);
                    System.out.println("Value for key '" + key + "': " + value);
                } else if (operation.equalsIgnoreCase("delete")) {
                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();
                    service.delete(key);
                } else {
                    System.out.println("Invalid operation");
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

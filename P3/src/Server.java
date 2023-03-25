import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Start the Server
public class Server {
    public static void main(String[] args) {
        try {
            // assign port number to portNumber
            int portNumber = Integer.parseInt(args[0]);
            // Create an instance of the RMI service implementation
            KeyValueService service = new KeyValueServiceImpl(portNumber);
            // Perform some operations on the key-value store
            service.put("yune", "best");
            service.put("apple", "red");
            service.put("peach", "pink");
            service.put("watermelon", "green");
            service.put("pear", "yellow");
            // Check if the object has already been exported
            if (UnicastRemoteObject.unexportObject(service, false)) {
                System.out.println("Unexported existing object");
            }

            // Export the RMI service implementation
            KeyValueService stub = (KeyValueService) UnicastRemoteObject.exportObject(service, portNumber);

            // Bind the RMI service implementation to a name in the RMI registry
            Registry registry = LocateRegistry.createRegistry(portNumber);
            registry.bind("KeyValueService", stub);

            System.out.println("Server ready on " + portNumber);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

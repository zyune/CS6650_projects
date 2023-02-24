import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Start the Server
public class Server {
    public static void main(String[] args) {
        try {
            // Create an instance of the RMI service implementation
            KeyValueService service = new KeyValueServiceImpl();

            // Check if the object has already been exported
            if (UnicastRemoteObject.unexportObject(service, false)) {
                System.out.println("Unexported existing object");
            }

            // Export the RMI service implementation
            KeyValueService stub = (KeyValueService) UnicastRemoteObject.exportObject(service, 0);

            // Bind the RMI service implementation to a name in the RMI registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("KeyValueService", stub);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

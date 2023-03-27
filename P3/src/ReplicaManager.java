import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import java.rmi.server.UnicastRemoteObject;

public class ReplicaManager {
    private static List<KeyValueService> replicas;

    public ReplicaManager(int[] replicaAddresses) throws Exception {
        replicas = new ArrayList<>();
        for (int address : replicaAddresses) {
            try {
                String serviceName = "service" + address;
                KeyValueService service = new KeyValueServiceImpl(address);
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
                KeyValueService stub = (KeyValueService) UnicastRemoteObject.exportObject(service, address);

                // Bind the RMI service implementation to a name in the RMI registry
                Registry registry = LocateRegistry.createRegistry(address);
                setReplicas(service);
                registry.bind(serviceName, stub);

                System.out.println("Server ready on " + address);

            } catch (Exception e) {
                System.err.println("Server exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static List<KeyValueService> getReplicas() {
        return replicas;
    }

    public static void setReplicas(KeyValueService service) {
        replicas.add(service);
    }

    public static void main(String[] args) throws Exception {
        int[] replicaAddresses = { 8898, 8899, 8900, 8901, 8902 };
        ReplicaManager rm = new ReplicaManager(replicaAddresses);
        System.out.println(rm);
        System.out.println("successfully created 5 instance");
    }
}


// Define the RMI service interface
import java.rmi.*;

public interface KeyValueService extends Remote {
    void put(String key, String value) throws RemoteException;

    String get(String key) throws RemoteException;

    void delete(String key) throws RemoteException;
}

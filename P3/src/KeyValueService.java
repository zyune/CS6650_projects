
// Define the RMI service interface
import java.rmi.*;
import java.util.Map;

public interface KeyValueService extends Remote {

    void put(String key, String value) throws RemoteException;

    String get(String key) throws RemoteException;

    void delete(String key) throws RemoteException;

    // boolean prepare();
    String prepare(String key, String value) throws RemoteException; // put和delete的prepare

    String getPrepare(String key, String value) throws RemoteException;

    String go(String key, String value) throws RemoteException;

    String commit(String key) throws RemoteException;

    String abort(String key) throws RemoteException;

    Map<String, String> getStore() throws RemoteException;
}

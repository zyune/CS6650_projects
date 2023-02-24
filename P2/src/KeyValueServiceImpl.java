import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
// Implement the RMI service interface
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KeyValueServiceImpl extends UnicastRemoteObject implements KeyValueService {
    private Map<String, String> store;
    private ExecutorService executor;

    public KeyValueServiceImpl() throws RemoteException {
        super();
        this.store = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(10); // use a thread pool with 10 threads
    }

    public void put(String key, String value) throws RemoteException {
        executor.submit(() -> {
            synchronized (store) {
                store.put(key, value);
            }
        });
    }

    public String get(String key) throws RemoteException {
        Future<String> future = executor.submit(() -> {
            synchronized (store) {
                return store.get(key);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RemoteException("Error while getting value for key: " + key, e);
        }
    }

    public void delete(String key) throws RemoteException {
        executor.submit(() -> {
            synchronized (store) {
                store.remove(key);
            }
        });
    }
}

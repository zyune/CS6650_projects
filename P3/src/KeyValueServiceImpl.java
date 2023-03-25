
// Implement the RMI service interface
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KeyValueServiceImpl extends UnicastRemoteObject implements KeyValueService {
    public Map<String, String> store;
    private ExecutorService executor;
    private int number;

    public KeyValueServiceImpl(int port) throws RemoteException {
        super();
        this.number = port;
        this.store = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(10); // use a thread pool with 10 threads
    }

    public Map<String, String> getStore() {

        return this.store;
    }

    public void put(String key, String value) throws RemoteException {
        // Phase 1: Prepare
        List<Future<String>> futures = new ArrayList<>();
        for (KeyValueService replica : ReplicaManager.getReplicas()) {
            if (!replica.equals(this)) {
                futures.add(executor.submit(() -> replica.prepare(key, value)));
            }
        }
        for (Future<String> future : futures) {
            try {
                if (!future.get().equals("ACK")) {
                    // Abort the operation if any replica sends a NACK
                    abort(key);
                    return;
                }
            } catch (InterruptedException | ExecutionException e) {
                // Abort the operation if a replica fails to respond
                abort(key);
                return;
            }
        }

        // Phase 2: Commit
        futures.clear();
        for (KeyValueService replica : ReplicaManager.getReplicas()) {
            futures.add(executor.submit(() -> replica.go(key, value)));
        }
        for (Future<String> future : futures) {
            try {
                if (!future.get().equals("ACK")) {
                    // Log error: Commit failed on replica
                }
            } catch (InterruptedException | ExecutionException e) {
                // Log error: Commit failed on replica
            }
        }

        // Update the local store
        synchronized (store) {
            store.put(key, value);
        }
    }

    public String prepare(String key, String value) throws RemoteException {
        // Check if the key already exists
        if (store.containsKey(key)) {
            // Log error: Key already exists
            return "NACK";
        }
        return "ACK";
    }

    public String getPrepare(String key, String value) throws RemoteException {
        // Check if the key already exists
        if (store.containsKey(key)) {
            // key exits return the key value
            return "ACK";
        }
        return "ACK";
    }

    public String go(String key, String value) throws RemoteException {
        // Update the local store
        synchronized (store) {
            store.put(key, value);
        }
        return "ACK";
    }

    public String commit(String key) throws RemoteException {
        // Update the local store
        synchronized (store) {
            store.remove(key);
        }
        return "ACK";
    }

    public String abort(String key) throws RemoteException {
        // Log message: Aborted update for key
        return "ACK";
    }

    public String get(String key) throws RemoteException {
        // Phase 1: Prepare
        List<Future<String>> futures = new ArrayList<>();
        for (KeyValueService replica : ReplicaManager.getReplicas()) {
            if (!replica.equals(this)) {
                futures.add(executor.submit(() -> replica.getPrepare(key, null)));
            }
        }
        for (Future<String> future : futures) {
            try {
                if (!future.get().equals("ACK")) {
                    // Abort the operation if any replica sends a NACK
                    return null;
                }
            } catch (InterruptedException | ExecutionException e) {
                // Abort the operation if a replica fails to respond
                return null;
            }
        }

        // Phase 2: Commit
        futures.clear();
        for (KeyValueService replica : ReplicaManager.getReplicas()) {
            futures.add(executor.submit(() -> replica.getStore().get(key)));
        }
        for (Future<String> future : futures) {
            try {
                String value = future.get();
                if (value != null) {
                    return value;
                }
            } catch (InterruptedException | ExecutionException e) {
                // Log error: Get failed on replica
                System.out.println("No such object in key value store");
            }
        }

        return null;
    }

    public void delete(String key) throws RemoteException {
        // Phase 1: Prepare
        List<Future<String>> futures = new ArrayList<>();
        for (KeyValueService replica : ReplicaManager.getReplicas()) {
            if (!replica.equals(this)) {
                futures.add(executor.submit(() -> replica.getPrepare(key, null)));
            }
        }
        for (Future<String> future : futures) {
            try {
                if (!future.get().equals("ACK")) {
                    // Abort the operation if any replica sends a NACK
                    abort(key);
                    return;
                }
            } catch (InterruptedException | ExecutionException e) {
                // Abort the operation if a replica fails to respond
                abort(key);
                return;
            }
        }

        // Phase 2: Commit
        futures.clear();
        for (KeyValueService replica : ReplicaManager.getReplicas()) {
            futures.add(executor.submit(() -> replica.commit(key)));
        }
        for (Future<String> future : futures) {
            try {
                if (!future.get().equals("ACK")) {
                    // Log error: Commit failed on replica
                    System.out.println("Commit failed on replica");
                }
            } catch (InterruptedException | ExecutionException e) {
                // Log error: Commit failed on replica
                System.out.println("Commit failed on replica" + e);
            }
        }
        // Delete the key from the local store

    }
}
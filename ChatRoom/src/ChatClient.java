import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1099;

    public static void main(String[] args) {
        try {
            // lookup the remote chat server object
            ChatServerInterface server = (ChatServerInterface) Naming
                    .lookup("rmi://" + SERVER_HOST + ":" + SERVER_PORT + "/ChatServer");

            // create the client object and register with the server
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            System.out.print("Enter client ID: ");
            String clientID = scanner.nextLine();
            ChatClientInterface client = new ChatClientImpl(clientID);
            server.register(client);

            // read user input and send messages to the server
            while (true) {
                String message = scanner.nextLine();
                server.broadcast(message, client);
            }
        } catch (Exception e) {
            System.err.println("Chat client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// This class implements the ChatClientInterface, which is the remote interface
// that clients use to receive messages from the server.
class ChatClientImpl extends UnicastRemoteObject implements ChatClientInterface {
    private String ClientID;

    public ChatClientImpl(String cid) throws RemoteException {
        this.ClientID = cid;
    }

    public String getClientID() {
        return this.ClientID;
    }

    private void setClientID(String cid) {
        this.ClientID = cid;
    }

    public void receiveMessage(ChatClientInterface c, String message) throws RemoteException {
        System.out.println("Client " + c.getClientID() + ":" + message);
    }
}

#Key-Value Pair Server and Client System using RMI

This is a simple key-value pair server and client system implemented using Remote Method Invocation (RMI) in Java. The system allows the client to perform CRUD (Create, Read, Update, Delete) operations on a key-value store by communicating with the server through RMI.

##Components

The system consists of two components:

1. Server: This component provides the RMI service implementation of the key-value store. It receives requests from the client and performs the corresponding CRUD operation on the key-value store.

2. Client: This component provides a command-line interface for the user to interact with the key-value store. The user can enter "put", "get", or "delete" to perform the corresponding operation, or "quit" to exit the system.

## Code

The server and client code are provided in separate Java classes as follows:

1. `KeyValueService`: This is the RMI service interface that defines the methods for performing CRUD operations on the key-value store.

2. `KeyValueServiceImpl`: This is the implementation of the `KeyValueService` interface. It provides a thread-safe implementation of the key-value store using a hashmap and a thread pool.

3. `Server`: This is the main class for the server component. It exports the `KeyValueServiceImpl` as an RMI service, registers it with the RMI registry, and makes it available to the client.

4. `Client`: This is the main class for the client component. It uses the RMI registry to look up the `KeyValueService` RMI service implementation and provides a simple command-line interface to allow the user to perform CRUD operations on the key-value store.

## Usage

To use the system, follow these steps:

1. Compile the server and client code using the Java compiler.

2. Start the server by running the Server class.

3. Start the client by running the Client class.

4. Follow the instructions on the command-line interface to perform CRUD operations on the key-value store.

## Conclusion

This key-value pair server and client system using RMI provides a basic implementation of a distributed key-value store that can be accessed remotely by a client. The system is thread-safe, and the server can handle multiple requests from multiple clients concurrently. However, this implementation does not provide any data persistence, and the key-value store will be lost if the server is shut down.

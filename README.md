# file-catalog
A client-server system built with Java RMI and JPA that allows users to upload and download files.

## How to build
Run the following command in the root project directory:
```bash
mvn package
```

## How to run
Start the server by executing the server.jar JAR located in .\server\target:
```bash
java -jar server.jar <port> 
```

Start the client by executing the client.jar JAR located in .\client\target:
```bash
java -jar client.jar <host>
```

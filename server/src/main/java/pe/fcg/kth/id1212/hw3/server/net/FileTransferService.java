package pe.fcg.kth.id1212.hw3.server.net;

import pe.fcg.kth.id1212.hw3.common.model.*;
import pe.fcg.kth.id1212.hw3.server.model.FileRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileTransferService implements Runnable {
    private int port;
    private FileRepository fileRepository;
    private ConcurrentMap<SessionContext, ClientEndpoint> clientEndpoints = new ConcurrentHashMap<>();
    private boolean isWorking;

    public FileTransferService(int port, FileRepository fileRepository) {
        this.port = port;
        this.fileRepository = fileRepository;
        isWorking = true;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("FileEntity transfer service listening on port " + port + "...");
            while(isWorking) {
                Socket clientSocket = serverSocket.accept();
                startNewClientSession(clientSocket);
            }
        } catch(IOException e) {
            System.err.println("Failure to establish server socket (port=" + port + ").");
        }
    }

    public void disconnect(SessionContext sessionContext) {
        clientEndpoints.get(sessionContext).stop();
        clientEndpoints.remove(sessionContext);
    }

    private void startNewClientSession(Socket clientSocket) {
        ClientEndpoint clientEndpoint = new ClientEndpoint(this, clientSocket);
        Thread thread = new Thread(clientEndpoint);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    void addClientEndpoint(SessionContext sessionContext, ClientEndpoint clientEndpoint) {
        clientEndpoints.put(sessionContext, clientEndpoint);
    }

    UploadResponse upload(UploadRequest req) {
        return fileRepository.upload(req);
    }

    DownloadResponse download(DownloadRequest req) {
        return fileRepository.download(req);
    }
}

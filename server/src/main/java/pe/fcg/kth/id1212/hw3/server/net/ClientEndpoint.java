package pe.fcg.kth.id1212.hw3.server.net;

import pe.fcg.kth.id1212.hw3.common.model.*;
import pe.fcg.kth.id1212.hw3.common.net.NetUtils;

import java.io.IOException;
import java.net.Socket;

public class ClientEndpoint implements Runnable {
    private FileTransferService fileTransferService;
    private Socket clientSocket;
    private boolean isWorking;

    ClientEndpoint(FileTransferService fileTransferService, Socket clientSocket) {
        this.fileTransferService = fileTransferService;
        this.clientSocket = clientSocket;
    }

    public void run() {
        isWorking = true;
        try {
            fileTransferService.addClientEndpoint(receiveSessionContext(), this);
            while(isWorking) {
                processRequest(receiveRequest());
            }
        } catch(Exception e) {
            // connection lost
        }
    }

    private void processRequest(Object request) throws IOException {
        if(request instanceof UploadRequest) {
            processUploadRequest((UploadRequest) request);
        } else if(request instanceof  DownloadRequest) {
            processDownloadRequest((DownloadRequest) request);
        }
    }

    private void processUploadRequest(UploadRequest request) throws IOException {
        UploadResponse response = fileTransferService.upload(request);
        NetUtils.send(clientSocket, response);
    }

    private void processDownloadRequest(DownloadRequest request) throws IOException {
        DownloadResponse response = fileTransferService.download(request);
        NetUtils.send(clientSocket, response);
    }

    private Object receiveRequest() throws IOException {
        Object received = NetUtils.receive(clientSocket);
        if(!(received instanceof UploadRequest) && !(received instanceof DownloadRequest)) {
            throw new RuntimeException("Unexpected type received. Expected UploadRequest or DownloadRequest.");
        }
        return received;
    }

    private SessionContext receiveSessionContext() throws IOException {
        Object received = NetUtils.receive(clientSocket);
        if(!(received instanceof SessionContext)) {
            throw new RuntimeException("Unexpected type received. Expected SessionContext.");
        }
        return (SessionContext)received;
    }

    void stop() {
        isWorking = false;
    }
}

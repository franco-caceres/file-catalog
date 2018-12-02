package pe.fcg.kth.id1212.hw3.client.net;

import pe.fcg.kth.id1212.hw3.common.model.*;
import pe.fcg.kth.id1212.hw3.common.net.NetUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerEndpoint {
    private static final int SO_TIMEOUT = 1_800_000;
    private Socket serverSocket;

    public void connect(String host, int port) throws IOException {
        serverSocket = new Socket();
        serverSocket.connect(new InetSocketAddress(host, port));
        serverSocket.setSoTimeout(SO_TIMEOUT);
    }

    public void disconnect() throws IOException {
        serverSocket.close();
    }

    public void sendSessionContext(SessionContext sessionContext) throws IOException {
        NetUtils.send(serverSocket, sessionContext);
    }

    public UploadResponse upload(UploadRequest request) throws IOException {
        NetUtils.send(serverSocket, request);
        return (UploadResponse) NetUtils.receive(serverSocket);
    }

    public DownloadResponse download(DownloadRequest request) throws IOException {
        NetUtils.send(serverSocket, request);
        return (DownloadResponse) NetUtils.receive(serverSocket);
    }
}

package pe.fcg.kth.id1212.hw3.client.controller;

import pe.fcg.kth.id1212.hw3.common.model.File;
import pe.fcg.kth.id1212.hw3.client.net.ServerEndpoint;
import pe.fcg.kth.id1212.hw3.common.model.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Controller {
    private final ServerEndpoint serverEndpoint = new ServerEndpoint();
    private FileCatalog fileCatalog;
    private SessionContext sessionContext = null;
    private String host;
    private NotificationHandler notificationHandler;

    public Controller(String host, NotificationHandler notificationHandler) throws RemoteException, NotBoundException, MalformedURLException {
        this.host = host;
        this.notificationHandler = notificationHandler;
        lookupFileCatalog(host);
    }

    public SignUpResponse signUp(String username, String password) throws RemoteException {
        SignUpRequest req = new SignUpRequest();
        req.setUsername(username);
        req.setPassword(password);
        return fileCatalog.signUp(req);
    }

    public LoginResponse login(String username, String password) throws IOException {
        LoginRequest req = new LoginRequest();
        req.setUsername(username);
        req.setPassword(password);
        req.setNotificationHandler(notificationHandler);
        LoginResponse res = fileCatalog.login(req);
        if(res.isSuccessful()) {
            sessionContext = res.getSessionContext();
            serverEndpoint.connect(host, res.getServerPort());
            serverEndpoint.sendSessionContext(sessionContext);
        }
        return res;
    }

    public void logout() throws IOException {
        LogoutRequest req = new LogoutRequest();
        req.setSessionContext(sessionContext);
        fileCatalog.logout(req);
        serverEndpoint.disconnect();
        sessionContext = null;
    }

    public UploadResponse upload(String filePath, Boolean readOnly) throws IOException {
        UploadRequest req = new UploadRequest();
        req.setSessionContext(sessionContext);
        Path path = Paths.get(filePath);
        File file = new File();
        file.setName(path.getFileName().toString());
        byte[] fileBytes = Files.readAllBytes(path);
        file.setContent(fileBytes);
        file.setSize(fileBytes.length);
        file.setReadOnly(readOnly);
        req.setFile(file);
        return serverEndpoint.upload(req);
    }

    public DownloadResponse download(String fileName, String targetDirectory) throws IOException {
        DownloadRequest req = new DownloadRequest();
        req.setSessionContext(sessionContext);
        req.setFileName(fileName);
        DownloadResponse res = serverEndpoint.download(req);
        writeToFileSystem(res.getFile(), targetDirectory);
        return res;
    }

    public ListResponse list() throws RemoteException {
        ListRequest req = new ListRequest();
        req.setSessionContext(sessionContext);
        return fileCatalog.list(req);
    }

    public DeleteResponse delete(String fileName) throws RemoteException {
        DeleteRequest req = new DeleteRequest();
        req.setSessionContext(sessionContext);
        req.setFileName(fileName);
        return fileCatalog.delete(req);
    }

    public void quit() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(notificationHandler, false);
    }

    private void lookupFileCatalog(String host) throws RemoteException, NotBoundException, MalformedURLException {
        fileCatalog = (FileCatalog) Naming.lookup("//" + host + "/" + FileCatalog.REGISTRY_NAME);
    }

    private void writeToFileSystem(File file, String targetDirectory) throws IOException {
        Path path = Paths.get(targetDirectory + java.io.File.separator + file.getName());
        Files.write(path, file.getContent());
    }
}

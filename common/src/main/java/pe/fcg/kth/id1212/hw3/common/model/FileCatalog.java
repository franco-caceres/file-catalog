package pe.fcg.kth.id1212.hw3.common.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileCatalog extends Remote {
    String REGISTRY_NAME = "FILE_CATALOG";

    SignUpResponse signUp(SignUpRequest req) throws RemoteException;
    LoginResponse login(LoginRequest req) throws RemoteException;
    void logout(LogoutRequest req) throws RemoteException;
    ListResponse list(ListRequest req) throws RemoteException;
    DeleteResponse delete(DeleteRequest req) throws RemoteException;
}

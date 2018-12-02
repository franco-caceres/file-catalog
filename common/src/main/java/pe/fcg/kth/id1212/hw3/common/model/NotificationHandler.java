package pe.fcg.kth.id1212.hw3.common.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationHandler extends Remote {
    void receive(Notification notification) throws RemoteException;
}

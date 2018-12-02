package pe.fcg.kth.id1212.hw3.server.startup;

import pe.fcg.kth.id1212.hw3.common.model.FileCatalog;
import pe.fcg.kth.id1212.hw3.server.controller.Controller;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {
    public static void main(String[] args) {
        int port;
        if(args.length == 1) {
            port = Integer.valueOf(args[0]);
            try {
                startRegistry();
                Naming.rebind(FileCatalog.REGISTRY_NAME, new Controller(port));
                System.out.println("Server running.");
            } catch(Exception e) {
                System.out.println("Unable to start server.");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println("Port argument missing.");
            System.exit(1);
        }
    }

    private static void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch(RemoteException re) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}

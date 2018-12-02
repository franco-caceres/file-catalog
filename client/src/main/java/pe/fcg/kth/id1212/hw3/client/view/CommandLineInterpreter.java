package pe.fcg.kth.id1212.hw3.client.view;

import pe.fcg.kth.id1212.hw3.client.controller.Controller;
import pe.fcg.kth.id1212.hw3.common.model.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class CommandLineInterpreter implements Runnable {
    private final Scanner in = new Scanner(System.in);
    private final Controller controller;
    private final ScreenTextProvider textProvider = new ScreenTextProvider();
    private boolean isWorking = false;

    public CommandLineInterpreter(String host) throws RemoteException, NotBoundException, MalformedURLException {
        controller = new Controller(host, new CommandLineNotificationHandler());
    }

    public void start() {
        if(isWorking) {
            return;
        }
        isWorking = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while(isWorking) {
                printScreenText();
                Command command = getNextCommand();
                switch (command.getType()) {
                    case SIGNUP:
                        String username = command.getArgs()[Command.USERNAME_POS];
                        String password = command.getArgs()[Command.PASSWORD_POS];
                        processResponse(controller.signUp(username, password));
                        break;
                    case LOGIN:
                        username = command.getArgs()[Command.USERNAME_POS];
                        password = command.getArgs()[Command.PASSWORD_POS];
                        processResponse(controller.login(username, password));
                        break;
                    case LOGOUT:
                        processLogout();
                        break;
                    case UPLOAD:
                        String path = command.getArgs()[Command.PATH_POS];
                        Boolean readOnly = Boolean.valueOf(command.getArgs()[Command.READONLY_POS]);
                        processResponse(controller.upload(path, readOnly));
                        break;
                    case DOWNLOAD:
                        String fileName = command.getArgs()[Command.FILENAME_POS];
                        String targetDirectory = command.getArgs()[Command.DIRECTORY_POS];
                        processResponse(controller.download(fileName, targetDirectory));
                        break;
                    case LIST:
                        processResponse(controller.list());
                        break;
                    case DELETE:
                        fileName = command.getArgs()[Command.FILENAME_POS];
                        processResponse(controller.delete(fileName));
                        break;
                    case CLEAR:
                        processClearNotifications();
                        break;
                    case QUIT:
                        quit();
                        break;
                    case UNKNOWN:
                        break;
                }
            }
        } catch(Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }

    private void processResponse(Object response) {
        textProvider.processResponse(response);
    }

    private void processLogout() throws IOException {
        controller.logout();
        textProvider.setLoggedOut();
    }

    private void processClearNotifications() {
        textProvider.clearNotifications();
        printScreenText();
    }

    private void printScreenText() {
        clearConsole();
        System.out.print(textProvider.getText());
    }

    private void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void quit() throws NoSuchObjectException {
        isWorking = false;
        controller.quit();
    }

    private Command getNextCommand() {
        String userInput = in.nextLine();
        return new Command(userInput);
    }

    private class CommandLineNotificationHandler extends UnicastRemoteObject implements NotificationHandler {
        CommandLineNotificationHandler() throws RemoteException {
        }

        @Override
        public void receive(Notification notification) {
            textProvider.processNotification(notification);
            printScreenText();
        }
    }
}

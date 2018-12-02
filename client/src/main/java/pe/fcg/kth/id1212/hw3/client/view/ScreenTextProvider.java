package pe.fcg.kth.id1212.hw3.client.view;

import pe.fcg.kth.id1212.hw3.common.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ScreenTextProvider {
    private String text = initialScreen();
    private List<String> notifications = new ArrayList<>();

    String getText() {
        String output = text;
        if(notifications.size() > 0) {
            output += "\n";
            output += "Notifications:\n";
            for(String notification : notifications) {
                output += notification;
            }
        }
        output += "\n";
        output += "> ";
        return output;
    }

    private String initialScreen() {
        return
            "Commands:\n" +
            "signup <username> <password>\n\tsign up to use the file catalog\n" +
            "login <username> <password>\n\tlogin to use the file catalog\n" +
            "quit\n\texit the application\n";
    }

    private String loggedInScreen() {
        return
            "Commands:\n" +
            "list\n\tlist all files in the file catalog\n" +
            "download <file name> <target directory>\n\tdownload the specified file to the specified directory\n" +
            "upload <path> <readonly[true, false]>\n\tupload the file located at the specified local path with the specified readonly option\n" +
            "delete <file name>\n\tdelete the specified file from the file catalog\n" +
            "clear\n\tclear the notifications\n" +
            "logout\n\tlogout of the file catalog\n";
    }

    void processResponse(Object response) {
        if(response instanceof SignUpResponse) {
            setTextForSignUpResponse((SignUpResponse) response);
        } else if(response instanceof LoginResponse) {
            setTextForLoginResponse((LoginResponse) response);
        } else if(response instanceof UploadResponse) {
            setTextForUploadResponse((UploadResponse) response);
        } else if(response instanceof DownloadResponse) {
            setTextForDownloadResponse((DownloadResponse) response);
        } else if(response instanceof ListResponse) {
            setTextForListResponse((ListResponse) response);
        } else if(response instanceof DeleteResponse) {
            setTextForDeleteResponse((DeleteResponse) response);
        }
    }

    private void setTextForSignUpResponse(SignUpResponse res) {
        String t = initialScreen();
        t +=  "\n";
        switch(res.getCode()) {
            case OK:
                t += "User successfully created.\n";
                break;
            case USERNAME_EXISTS:
                t += "A user already exists with that name.\n";
        }
        text = t;
    }

    private void setTextForLoginResponse(LoginResponse res) {
        String t = "";
        switch(res.getCode()) {
            case OK:
                t = loggedInScreen();
                t += "\n";
                t += "Logged in successfully.\n";
                break;
            case USERNAME_NOT_FOUND:
                t = initialScreen();
                t += "\n";
                t += "The username entered does not exist.\n";
                break;
            case INVALID_CREDENTIALS:
                t = initialScreen();
                t += "\n";
                t += "The credentials entered are invalid, please check that your password is correct.\n";
                break;
        }
        text = t;
    }

    private void setTextForUploadResponse(UploadResponse res) {
        String t = "";
        switch(res.getCode()) {
            case OK:
                t = loggedInScreen();
                t += "\n";
                t += "File uploaded successfully.\n";
                break;
            case NO_SESSION:
                t = initialScreen();
                t += "\n";
                t += "Please login first.\n";
                break;
            case FORBIDDEN:
                t = loggedInScreen();
                t += "\n";
                t += "You cannot modify the file because it is read-only and you are not the owner.\n";
                break;
        }
        text = t;
    }

    private void setTextForDownloadResponse(DownloadResponse res) {
        String t = "";
        switch(res.getCode()) {
            case OK:
                t = loggedInScreen();
                t += "\n";
                t += "File downloaded successfully.\n";
                break;
            case NO_SESSION:
                t = initialScreen();
                t += "\n";
                t += "Please login first.\n";
                break;
            case NOT_FOUND:
                t = loggedInScreen();
                t += "\n";
                t += "The specified file was not found in the file catalog.\n";
                break;
        }
        text = t;
    }

    private void setTextForListResponse(ListResponse res) {
        String t = "";
        switch(res.getCode()) {
            case OK:
                t = loggedInScreen();
                t += "\n";
                t += "Files:\n";
                t+= "File Name\t\tSize(bytes)\t\tOwner\t\tRead-only\n";
                for(File file : res.getFiles()) {
                    t += file.getName() + "\t\t" + file.getSize() + "\t\t\t" + file.getOwner().getName() + "\t" + file.isReadOnly() + "\n";
                }
                break;
            case NO_SESSION:
                t = initialScreen();
                t += "\n";
                t += "Please login first.\n";
                break;
        }
        text = t;
    }

    private void setTextForDeleteResponse(DeleteResponse res) {
        String t = "";
        switch(res.getCode()) {
            case OK:
                t = loggedInScreen();
                t += "\n";
                t += "File deleted successfully.\n";
                break;
            case NO_SESSION:
                t = initialScreen();
                t += "\n";
                t += "Please login first.\n";
                break;
            case NOT_FOUND:
                t = loggedInScreen();
                t += "\n";
                t += "The specified file was not found in the file catalog.\n";
                break;
            case FORBIDDEN:
                t = loggedInScreen();
                t += "\n";
                t += "You cannot modify the file because it is read-only and you are not the owner.\n";
                break;
        }
        text = t;
    }

    void processNotification(Notification notification) {
        String notificationText = notification.getUsername() + " has";
        switch(notification.getAction()) {
            case READ:
                notificationText += " downloaded";
                break;
            case UPDATE:
                notificationText += " updated";
                break;
            case DELETE:
                notificationText += " deleted";
                break;
        }
        notificationText += " " + notification.getFilename() + "\n";
        notifications.add("[" + new Date() + "] " + notificationText);
    }

    void setLoggedOut() {
        text = initialScreen();
    }

    void clearNotifications() {
        notifications.clear();
    }
}

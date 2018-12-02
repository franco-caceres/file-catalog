package pe.fcg.kth.id1212.hw3.client.view;

class Command {
    public enum Type {
        SIGNUP, LOGIN, LOGOUT, UPLOAD, DOWNLOAD, LIST, DELETE, QUIT, UNKNOWN, CLEAR
    }
    static final int USERNAME_POS = 0;
    static final int PASSWORD_POS = 1;
    static final int PATH_POS = 0;
    static final int READONLY_POS = 1;
    static final int FILENAME_POS = 0;
    static final int DIRECTORY_POS = 1;
    private Type type;
    private String[] args;

    Command(String userInput) {
        parse(userInput);
    }

    private void parse(String userInput) {
        String[] parts = userInput.split("\\s+");
        try {
            type = Type.valueOf(parts[0].toUpperCase());
        } catch(Exception e) {
            type = Type.UNKNOWN;
        }
        args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);
    }

    Type getType() {
        return type;
    }

    String[] getArgs() {
        return args;
    }
}

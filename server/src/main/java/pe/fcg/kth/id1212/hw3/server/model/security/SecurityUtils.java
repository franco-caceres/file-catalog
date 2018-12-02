package pe.fcg.kth.id1212.hw3.server.model.security;

import java.security.MessageDigest;

public class SecurityUtils {
    public static String getPasswordHash(String username, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            return new String(md.digest());
        } catch(Exception e) {
            return null;
        }
    }
}

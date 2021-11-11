package org.duckdns.androidghost77.gamelove.service;

public interface PasswordService {

    byte[] hashPassword(char[] password, byte[] salt);

    boolean checkPassword(char[] password, byte[] hash, byte[] salt);

}

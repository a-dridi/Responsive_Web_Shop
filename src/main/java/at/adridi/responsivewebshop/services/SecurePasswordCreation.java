/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * Class used to create or recrate passwords for admin or user accounts
 *
 * @author A.Dridi
 */
public class SecurePasswordCreation {

    public String getHashedMD5Password(String password, byte[] salt) {
        String hashedPassword = null;

        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            md5MessageDigest.update(salt);
            byte[] hashBytes = md5MessageDigest.digest(password.getBytes());
            StringBuilder passwordStringBuilder = new StringBuilder();
            for (int i = 0; i < hashBytes.length; i++) {
                passwordStringBuilder.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = passwordStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashedPassword;
    }

    /**
     * This salt should not be changed. The same salt is also needed to recrate
     * hash of the password to allow login.
     *
     * public byte[] getSalt() throws NoSuchAlgorithmException,
     * NoSuchProviderException { SecureRandom generateSaltSr =
     * SecureRandom.getInstance("SHA1PRNG", "SUN"); byte[] newSalt = new
     * byte[16]; generateSaltSr.nextBytes(newSalt); return newSalt; }
     */
    /**
     * This salt should not be changed. The same salt is also needed to recrate
     * hash of the password to allow login.
     */
    public byte[] getSalt() {
        byte[] newSalt = new byte[16];
        newSalt[0] = 4;
        newSalt[1] = 1;
        newSalt[2] = 5;
        newSalt[3] = 2;
        newSalt[4] = 4;
        newSalt[5] = 2;
        newSalt[6] = 2;
        newSalt[7] = 1;
        newSalt[8] = 2;
        newSalt[9] = 5;
        newSalt[10] = 2;
        newSalt[11] = 1;
        newSalt[12] = 1;
        newSalt[13] = 4;
        newSalt[14] = 6;
        newSalt[15] = 1;

        return newSalt;
    }
}

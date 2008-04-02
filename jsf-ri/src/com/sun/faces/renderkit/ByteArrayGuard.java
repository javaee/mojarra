/*
 * $Id: ByteArrayGuard.java,v 1.13 2006/05/31 21:13:05 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.renderkit;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.faces.FacesException;

import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.Util;

/**
 * <p>This utility class is to provide both encryption and
 * decryption <code>Ciphers</code> to <code>ResponseStateManager</code>
 * implementations wishing to provide encryption support.</p>
 * 
 * <p>The algorithm used to encrypt byte array is 3DES with CBC.</p>
 *  
 * <p>Original author Inderjeet Singh, J2EE Blue Prints Team. Modified to suit JSF
 * needs.</p> 
 */
public final class ByteArrayGuard {


     // Log instance for this class
    private static final Logger LOGGER =
            Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);
    private static final int IV_LENGTH = 8;        
    private static final int KEY_LENGTH = 24;
      
    private static Cipher NULL_CIPHER = new NullCipher();               
    
    private Cipher decryptCipher = NULL_CIPHER;
    private Cipher encryptCipher = NULL_CIPHER;  

    // ------------------------------------------------------------ Constructors


    /**
     * Constructs a new <code>ByteArrayGuard</code> using the specified
     * <code>keyLength</code>, <code>macLength</code>, <code>ivLength</code>.    
     * @param password the password to seed the encryption 
     */
    public ByteArrayGuard(String password) {
       
        if (password != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "Client state saving encryption enabled.");
            }
            byte[] passwordKey = convertPasswordToKey(password.getBytes());           
            try {
                SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
                SecretKeyFactory keygen = SecretKeyFactory.getInstance("DESede");
                encryptCipher =
                      getBlockCipherForEncryption(keygen, prng, passwordKey);
                byte[] iVector = encryptCipher.getIV();
                decryptCipher =
                      getBlockCipherForDecryption(keygen, 
                                                  prng, 
                                                  passwordKey,
                                                  iVector);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "Unexpected exception initializing encryption."
                               + "  No encryption will be performed.",
                               e);
                }               
                               
                encryptCipher = NULL_CIPHER;
                decryptCipher = NULL_CIPHER;              
               
            }
        }       
    }


    // ---------------------------------------------------------- Public Methods    


    public Cipher getEncryptionCipher() {
        return encryptCipher;
    }
    
    public Cipher getDecryptionCipher() {
        return decryptCipher;
    }
    

    // --------------------------------------------------------- Private Methods
    


    /**
     * This method converts the specified password into a key in a
     * deterministic manner. The key is then usable for creating ciphers
     * and MACs.
     * 
     * @param password plain text password
     * 
     * @return a byte array containing a key based on the specified
     * password. The length of the returned byte array is KEY_LENGTH.
     */
    private byte[] convertPasswordToKey(byte[] password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] seed = md.digest(password);

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(seed);

            byte[] rawkey = new byte[KEY_LENGTH];
            random.nextBytes(rawkey);
            return rawkey;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /** 
     * Obtain a <code>Cipher</code> for decrypting data.
     * @param keyGen
     * @param random
     * @param rawKey must be 24 bytes in length.
     * @param iv initialization vector  @return a 3DES block cipher to be used for decryption based on the
     * specified key
     * @return an initialized <code>Cipher</code> for decryption
     */
    private Cipher getBlockCipherForDecryption(SecretKeyFactory keyGen,
                                               SecureRandom random,
                                               byte[] rawKey,
                                               byte[] iv) {

        try {           
            DESedeKeySpec keyspec = new DESedeKeySpec(rawKey);
            Key key = keyGen.generateSecret(keyspec);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivspec, random);
            return cipher;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /** 
     * Obtain a <code>Cipher</code> for encrypting data.
     * @param keyGen
     * @param random
     * @param rawKey must be 24 bytes in length.  @return a 3DES block cipher to be used for encryption based on the
     * specified key
     * @return an initialized <code>Cipher</code> for decryption
     */
    private Cipher getBlockCipherForEncryption(SecretKeyFactory keyGen,
                                               SecureRandom random,
                                               byte[] rawKey) {

        try {           
            DESedeKeySpec keyspec = new DESedeKeySpec(rawKey);
            Key key = keyGen.generateSecret(keyspec);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivspec, random);
            return cipher;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }

    
}

/*
 * $Id: ByteArrayGuard.java,v 1.10 2006/03/29 22:38:35 rlubke Exp $
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
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.FacesException;
import javax.naming.InitialContext;

import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

/**
 * This utility class provides services to encrypt or decrypt a byte array.
 * The algorithm used to encrypt byte array is 3DES with CBC
 * The algorithm used to create the message authentication code (MAC) is SHA1
 * <p/>
 * Original author Inderjeet Singh, J2EE Blue Prints Team. Modified to suit JSF
 * needs.
 */
public final class ByteArrayGuard {


    // Log instance for this class
    private static final Logger logger =
          Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);
    private static final int DEFAULT_IV_LENGTH = 8;
    private static final int DEFAULT_KEY_LENGTH = 24;
    private static final int DEFAULT_MAC_LENGTH = 20;

    private static ByteArrayGuard byteArrayGuard;

    private final Object decLock = new Object();
    private final Object encLock = new Object();
    private final int ivLength;
    private final int keyLength;
    private final int macLength;

    private Cipher decryptCipher = null;
    private Cipher encryptCipher = null;
    private SecretKeyFactory keygen = null;
    private SecureRandom prng = null;
    private byte[] PASSWORD_KEY = null;
    private byte[] iVector = null;

    // ------------------------------------------------------------ Constructors


    /**
     * Constructs a new <code>ByteArrayGuard</code> using the specified
     * <code>keyLength</code>, <code>macLength</code>, <code>ivLength</code>.
     *
     * @param keyLength the length of the key used for encryption
     * @param macLength the length of the message authentication used
     * @param ivLength  length of the initialization vector used by the block cipher
     */
    private ByteArrayGuard(int keyLength, int macLength, int ivLength) {

        this.keyLength = keyLength;
        this.macLength = macLength;
        this.ivLength = ivLength;

        if (PASSWORD_KEY == null) {
            InitialContext iContext = null;
            try {
                iContext = new InitialContext();
                String password = (String) iContext
                      .lookup(RIConstants.CLIENT_STATE_ENC_PASSWORD_ENTRY_NAME);
                if (password != null) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE,
                                   "Client state saving encryption enabled.");
                    }
                    PASSWORD_KEY = convertPasswordToKey(password.getBytes());
                    try {
                        prng = SecureRandom.getInstance("SHA1PRNG");
                        keygen = SecretKeyFactory.getInstance("DESede");
                        encryptCipher =
                              getBlockCipherForEncryption(PASSWORD_KEY);
                        iVector = encryptCipher.getIV();
                        decryptCipher =
                              getBlockCipherForDecryption(PASSWORD_KEY,
                                                          iVector);
                    } catch (Exception e) {
                        if (logger.isLoggable(Level.SEVERE)) {
                            logger.log(Level.SEVERE,
                                       "Unexpected exception initializing encryption."
                                       + "  No encryption will be performed.",
                                       e);
                        }
                        PASSWORD_KEY = null;
                        keygen = null;
                        encryptCipher = null;
                        decryptCipher = null;
                        iVector = null;
                        prng = null;
                    }
                }
            } catch (Exception ne) {
                // i18n
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE,
                               "Client state saving encryption disabled.",
                               ne);
                }
            } finally {
                if (iContext != null) {
                    try {
                        iContext.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

    }

    // ---------------------------------------------------------- Public Methods


    public static synchronized ByteArrayGuard getInstance() {

        if (byteArrayGuard == null) {
            byteArrayGuard = new ByteArrayGuard(DEFAULT_KEY_LENGTH,
                                                DEFAULT_MAC_LENGTH,
                                                DEFAULT_IV_LENGTH);
        }
        return byteArrayGuard;

    }


    /**
     * Decrypts the specified byte array using the specified password, and
     * generates an inputstream from it. The file must be encrypted by the
     * above method for encryption. The method also verifies the MAC. It
     * uses the IV present in the file for decryption.
     *
     * @param securedata The encrypted data (including mac and initialization
     *                   vector) that needs to be decrypted
     *
     * @return A byte array containing the decrypted contents
     */
    public byte[] decrypt(byte[] securedata) {

        if (PASSWORD_KEY != null) {
            try {
                // Extract MAC
                byte[] macBytes = new byte[macLength];
                System.arraycopy(securedata, 0, macBytes, 0, macBytes.length);
                // Extract initialization vector used for encryption
                byte[] iv = new byte[ivLength];
                System.arraycopy(securedata, macBytes.length, iv, 0, iv.length);

                // Extract encrypted data
                byte[] encdata =
                      new byte[securedata.length - macBytes.length - iv.length];
                System.arraycopy(securedata,
                                 macBytes.length + iv.length,
                                 encdata,
                                 0,
                                 encdata.length);

                Mac mac = getMac(PASSWORD_KEY);
                // verify MAC by regenerating it and comparing it with the received value                           
                mac.update(encdata);
                byte[] macBytesCalculated = mac.doFinal();
                if (Arrays.equals(macBytes, macBytesCalculated)) {
                    // decrypt data only if the MAC was valid  
                    synchronized (decLock) {
                        return decryptCipher.doFinal(encdata);
                    }
                } else {
                    throw new IOException(
                          "Could not Decrypt Secure View State, passwords did not match.");
                }
            } catch (Exception e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, e.getMessage(), e.getCause());
                }
                throw new FacesException(e);
            }
        } else {
            return securedata;
        }

    }


    /**
     * Encrypts the specified plaindata using the specified password. It also
     * stores the MAC and the IV in the output. The 20-byte MAC is stored
     * first, followed by the 8-byte IV, followed by the encrypted
     * contents of the file.
     *
     * @param plaindata The plain text that needs to be encrypted
     *
     * @return The encrypted contents
     */
    public byte[] encrypt(byte[] plaindata) {

        if (PASSWORD_KEY != null) {
            try {
                // encrypt the plaintext

                byte[] encdata;
                synchronized (encLock) {
                    encdata = encryptCipher.doFinal(plaindata);
                }
                Mac mac = getMac(PASSWORD_KEY);
                mac.update(encdata);
                // generate MAC
                byte[] macBytes = mac.doFinal();

                // concat byte arrays for MAC, IV, and encrypted data
                // Note that the order is important here. MAC and IV are
                // of fixed length and need to appear before the encrypted data
                // for easy extraction while decrypting.
                byte[] tmp = concatBytes(macBytes, iVector);
                return concatBytes(tmp, encdata);
            } catch (Exception e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, e.getMessage(), e.getCause());
                }
                throw new FacesException(e);
            }
        } else {
            return plaindata;
        }

    }

    // --------------------------------------------------------- Private Methods


    /**
     * This method concatenates two byte arrays.
     *
     * @param array1 first byte array to be concatenated
     * @param array2 second byte array to be concatenated
     *
     * @return a byte array of array1||array2
     */
    private static byte[] concatBytes(byte[] array1, byte[] array2) {

        byte[] cBytes = new byte[array1.length + array2.length];
        try {
            System.arraycopy(array1, 0, cBytes, 0, array1.length);
            System.arraycopy(array2, 0, cBytes, array1.length, array2.length);
        } catch (Exception e) {
            throw new FacesException(e);
        }
        return cBytes;

    }


    /**
     * This method converts the specified password into a key in a
     * deterministic manner. The key is then usable for creating ciphers
     * and MACs.
     *
     * @param password plain text password
     *
     * @return a byte array containing a key based on the specified
     *         password. The length of the returned byte array is KEY_LENGTH.
     */
    private byte[] convertPasswordToKey(byte[] password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] seed = md.digest(password);

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(seed);

            byte[] rawkey = new byte[keyLength];
            random.nextBytes(rawkey);
            return rawkey;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /**
     * Obtain a <code>Cipher</code> for decrypting data.
     *
     * @param rawKey must be 24 bytes in length.
     * @param iv     initialization vector
     *
     * @return a 3DES block cipher to be used for decryption based on the
     *         specified key
     */
    private Cipher getBlockCipherForDecryption(byte[] rawKey,
                                               byte[] iv) {

        try {
            DESedeKeySpec keyspec = new DESedeKeySpec(rawKey);
            Key key = keygen.generateSecret(keyspec);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivspec, prng);
            return cipher;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /**
     * Obtain a <code>Cipher</code> for encrypting data.
     *
     * @param rawKey must be 24 bytes in length.
     *
     * @return a 3DES block cipher to be used for encryption based on the
     *         specified key
     */
    private Cipher getBlockCipherForEncryption(byte[] rawKey) {

        try {
            DESedeKeySpec keyspec = new DESedeKeySpec(rawKey);
            Key key = keygen.generateSecret(keyspec);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            byte[] iv = new byte[ivLength];
            prng.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivspec, prng);
            return cipher;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    private Mac getMac(byte[] rawKey) {

        try {
            Mac lMac = Mac.getInstance("HmacSHA1");
            SecretKeySpec key =
                  new SecretKeySpec(rawKey, 0, macLength, "HmacSHA1");
            lMac.init(key);
            return lMac;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }

}

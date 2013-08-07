/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.util;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.faces.FacesException;
import java.security.SecureRandom;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

/**
 * <p>This utility class is to provide both encryption and
 * decryption <code>Ciphers</code> to <code>ResponseStateManager</code>
 * implementations wishing to provide encryption support.</p>
 * 
 * <p>The algorithm used to encrypt byte array is AES with CBC.</p>
 *  
 * <p>Original author Inderjeet Singh, J2EE Blue Prints Team. Modified to suit JSF
 * needs.</p> 
 */
public final class ByteArrayGuardAESCTR {


     // Log instance for this class
    private static final Logger LOGGER = FacesLogger.RENDERKIT.getLogger();

    private static final int KEY_LENGTH = 128;
    private static final int IV_LENGTH = 16;

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_CODE = "AES/CTR/NoPadding";
    
    private SecretKey sk;
    
    private IvParameterSpec ivspec;
    
    private Charset utf8;

    // ------------------------------------------------------------ Constructors

    public ByteArrayGuardAESCTR() {

        try {
            setupKeyAndCharset();
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) { 
                LOGGER.log(Level.SEVERE,
                           "Unexpected exception initializing encryption."
                           + "  No encryption will be performed.",
                           e);
            }
            System.err.println("ERROR: Initializing Ciphers");
        }
    }

    // ---------------------------------------------------------- Public Methods    


    /**
     * This method:
     *    Encrypts bytes using a cipher.  
     *    Generates MAC for intialization vector of the cipher
     *    Generates MAC for encrypted data
     *    Returns a byte array consisting of the following concatenated together:
     *       |MAC for cnrypted Data | MAC for Init Vector | Encrypted Data |
     * @param bytes The byte array to be encrypted.
     * @return the encrypted byte array.
     */
    public String encrypt(String value) {
        String securedata = null;
        byte[] bytes = value.getBytes(utf8);
        try {
            Cipher encryptCipher = Cipher.getInstance(CIPHER_CODE);
            encryptCipher.init(Cipher.ENCRYPT_MODE, sk, ivspec);
            // encrypt the plaintext
            byte[] encdata = encryptCipher.doFinal(bytes);
            // Base64 encode the encrypted bytes
            securedata = DatatypeConverter.printBase64Binary(encdata);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           "Unexpected exception initializing encryption."
                           + "  No encryption will be performed.",
                           e);
            }
            return null;
        }
        return securedata;
    }

    public String decrypt(String value) throws InvalidKeyException {
        
        byte[] bytes = DatatypeConverter.parseBase64Binary(value);;
        
        try {
            Cipher decryptCipher = Cipher.getInstance(CIPHER_CODE);
            decryptCipher.init(Cipher.DECRYPT_MODE, sk, ivspec);

            byte[] plaindata = decryptCipher.doFinal(bytes);
            for (byte cur : plaindata) {
                if (cur < 0 || cur > 255) {
                    throw new InvalidKeyException("Invalid characters in decrypted value");
                }
            }
            return new String(plaindata, utf8);
        } catch (NoSuchAlgorithmException nsae) {
            throw new InvalidKeyException(nsae);
        } catch (NoSuchPaddingException nspe) {
            throw new InvalidKeyException(nspe);
        } catch (InvalidAlgorithmParameterException iape) {
            throw new InvalidKeyException(iape);
        } catch (IllegalBlockSizeException ibse) {
            throw new InvalidKeyException(ibse);
        } catch (BadPaddingException bpe) {
            throw new InvalidKeyException(bpe);
        }
    }
    
    // --------------------------------------------------------- Private Methods

    private void setupKeyAndCharset() {

        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(KEY_LENGTH);   // 256 if you're using the Unlimited Policy Files
            sk = kg.generateKey(); 
        } catch (Exception e) {
            throw new FacesException(e);
        }
        
        SecureRandom rand = new SecureRandom();
        byte[] iv = new byte[16];
        rand.nextBytes(iv);
        ivspec = new IvParameterSpec(iv);
            
        SortedMap<String,Charset> availableCharsets = Charset.availableCharsets();
        if (availableCharsets.containsKey("UTF-8")) {
            utf8 = availableCharsets.get("UTF-8");
        } else if (availableCharsets.containsKey("UTF8")) {
            utf8 = availableCharsets.get("UTF8");
        } else {
            throw new FacesException("Unable to get UTF-8 Charset.");
        }
        
    }

}

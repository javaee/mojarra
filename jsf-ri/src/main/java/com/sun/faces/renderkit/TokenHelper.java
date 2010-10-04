/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

package com.sun.faces.renderkit;

import com.sun.faces.util.FacesLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpSession;

/**
 * Provides static methods for creating and storing a secret key (generated
 * through {@link java.security.SecureRandom}) in the session and generating a
 * token from a JSF view id and the secret key. The token is
 * {@link java.security.MessageDigest} generated using MD5.
 * 
 */
public class TokenHelper {

    public final static String SECRET_KEY = "secret.key";

    protected static SecureRandom random = null;

    public static final String FORM_CLIENT_ID_ATTRIBUTE_NAME = "com.sun.faces.FORM_CLIENT_ID";

    private static Logger LOGGER = FacesLogger.RENDERKIT.getLogger();

    /**
     * Generate an MD5 hash based on the specified viewId and secret key.
     * 
     * @param viewId
     * @param secretKey
     * @return hash in String format
     */
     public static String generateToken(String viewId, Long secretKey) {
        //PENDING: consider performance implications as this is done for each
        // actionURL construction / token hidden field construction.
        // Perhaps a better structure in session so this is done once and
        // stored in session.
         String token = null;
         try {
             MessageDigest md = MessageDigest.getInstance("MD5");
             md.update(viewId.getBytes());
             md.update(secretKey.byteValue());
             token = toHex(md.digest());
         } catch (NoSuchAlgorithmException e) {
             throw new FacesException(e);
         }
         return token;
    }

    /**
     * Retrieve the token from the current request.
     * 
     * @param requestParameterMap
     * @return token value
     */
    @SuppressWarnings("unchecked")
    public static String getToken(Map requestParameterMap) {
        String token = null;
        Iterator iter = requestParameterMap.keySet().iterator();
        while (iter.hasNext()) {
            String keyName = (String)iter.next();
            if (keyName.contains(ResponseStateManager.VIEW_TOKEN_PARAM)) {
                token = (String)requestParameterMap.get(keyName);
                break;
            }
            
        }
        return token;
    }

    /**
     * Return the secret key from the current session.
     * 
     * @param sessionMap
     * @return secret key
     */
    @SuppressWarnings("unchecked")
    public static Long getSecretKey(Map sessionMap) {
        Long key = (Long) sessionMap.get(SECRET_KEY);
        return key;
    }

    /**
     * Creates a new secret key using SecureRandom
     * and stores it in the session under {@link #SECRET_KEY}.
     *
     * @param session
     * @throws NoSuchAlgorithmException
     */
    public static void setSecretKey(Map sessionMap)
        throws NoSuchAlgorithmException {
        if (random == null) {
            random = SecureRandom.getInstance("SHA1PRNG");
        }
        long key = random.nextLong();
        sessionMap.put(SECRET_KEY, key);
    }


    /**
     * Appends a request parameter with a token value generated by
     * {@link generateToken} using the parameter name
     * {@link javax.faces.render.ResponseStateManager#VIEW_TOKEN_PARAM}
     * namespaced with the enclosing form's client identifier (if available).
     * 
     * @param facesContext
     * @param actionURL
     * @return String containing the appended request parameter.
     */
    public static String appendToken(FacesContext facesContext, String viewId, String actionURL) {
        ExternalContext eContext = facesContext.getExternalContext();
        Map sessionMap = eContext.getSessionMap();
        String formClientId = (String)facesContext.getAttributes().get(FORM_CLIENT_ID_ATTRIBUTE_NAME);
        Long secretKey = getSecretKey(sessionMap);
        if (null == secretKey) {
            try {
                setSecretKey(sessionMap);
                secretKey = TokenHelper.getSecretKey(sessionMap);
            } catch (Exception e) {
                throw new FacesException("Could not generate secret key for token");
            }
        }
        String namingContainerString = "";
        if (null != formClientId) {
            namingContainerString = formClientId +
                UINamingContainer.getSeparatorChar(facesContext);
        }
        String appendedURL = actionURL
	    + "?"
            + namingContainerString
            + ResponseStateManager.VIEW_TOKEN_PARAM
            + "="
            + generateToken(viewId, secretKey);
        return appendedURL;
    }

    /**
     * Verifies that the token in the current request parameter Map is
     * the same as the token value generated by {@link generateToken}.
     */
    public static boolean verifyToken(FacesContext facesContext, String viewId) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestParameterMap = externalContext.getRequestParameterMap();
        Map sessionMap = externalContext.getSessionMap();
        String token = getToken(requestParameterMap);
        if (null == token) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Token not found in request");
            }
            return false;
        }
        Long secretKey = getSecretKey(sessionMap);
        if (null == secretKey) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Secret Key not set");
            }
            return false;
        }
        if (!token.equals(generateToken(viewId, secretKey))) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Token verification failed");
            }
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * <p>
     * Convert the specified byte array into a String of hexadecimal digit
     * characters.
     * </p>
     * 
     * @param buffer
     *            Byte array to be converted
     */
    private static String toHex(byte buffer[]) {

	StringBuffer sb = new StringBuffer(buffer.length * 2);
	for (int i = 0; i < buffer.length; i++) {
   	    sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
	    sb.append(Character.forDigit((buffer[i] & 0x0f), 16));
        }
        return sb.toString();
    }
}

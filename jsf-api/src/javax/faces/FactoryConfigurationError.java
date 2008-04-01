/*
 * $Id: FactoryConfigurationError.java,v 1.1 2001/11/09 23:44:06 edburns Exp $
 * 
 * Copyright (c) 1998-1999 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 * 
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

package javax.faces;

/**
 * Thrown when a problem with configuration with the Factories
 * exists. This error will typically be thrown when the class of a
 * factory specified in the system properties cannot be found
 * or instantiated.
 *
 * @since faces 1.0
 * @version 1.0
 */

public class FactoryConfigurationError extends Error {

    private Exception exception;

    /**
     * Create a new <code>FactoryConfigurationError</code> with no
     * detail mesage.
     */

     public FactoryConfigurationError() {
         super();
    	 this.exception = null; 
     }

    /**
     * Create a new <code>FactoryConfigurationError</code> with
     * the <code>String </code> specified as an error message.
     *
     * @param msg The error message for the exception.
     */
    
    public FactoryConfigurationError(String msg) {
        super(msg);
        this.exception = null;
    }


    /**
     * Create a new <code>FactoryConfigurationError</code> with a
     * given <code>Exception</code> base cause of the error.
     *
     * @param e The exception to be encapsulated in a
     * FactoryConfigurationError.
     */
    
    public FactoryConfigurationError(Exception e) {
        super(e.toString());
        this.exception = e;
    }

    /**
     * Create a new <code>FactoryConfigurationError</code> with the
     * given <code>Exception</code> base cause and detail message.
     *
     * @param e The exception to be encapsulated in a
     * FactoryConfigurationError
     * @param msg The detail message.
     * @param e The exception to be wrapped in a FactoryConfigurationError
     */
    
    public FactoryConfigurationError(Exception e, String msg) {
        super(msg);
        this.exception = e;
    }


    /**
     * Return the message (if any) for this error . If there is no
     * message for the exception and there is an encapsulated
     * exception then the message of that exception, if it exists will be 
     * returned. Else the name of the encapsulated exception will be
     * returned.
     *
     * @return The error message.
     */
    
    public String getMessage () {
        String message = super.getMessage ();
  
        if (message == null && exception != null) {
            return exception.getMessage();
        }

        return message;
    }
  
    /**
     * Return the actual exception (if any) that caused this exception to
     * be raised.
     *
     * @return The encapsulated exception, or null if there is none.
     */
    
    public Exception getException () {
        return exception;
    }
}








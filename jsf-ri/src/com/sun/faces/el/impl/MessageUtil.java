/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.sun.faces.el.impl;

import java.text.MessageFormat;

/**
 * <p>Utility class for generating parameterized messages.</p>
 *
 * @version $Id: MessageUtil.java,v 1.3 2004/02/06 18:54:37 rlubke Exp $
 */

public class MessageUtil {

    /**
     * <p>Returns a formatted message based on the provided template and
     * a single parameter.</p>
     *
     * @param pTemplate the base message
     * @param pArg0     parameter
     *
     * @return Returns a formatted message based on the provided template and
     *         a single parameter.
     */
    public static String getMessageWithArgs(String pTemplate, Object pArg0) {
        return MessageFormat.format(pTemplate, new Object[]{"" + pArg0});
    }


    /**
     * <p>Returns a formatted message based on the provided template and
     * provided parameter.</p>
     *
     * @param pTemplate the base message
     * @param pArg0     parameter 1
     * @param pArg1     parameter 2
     *
     * @return Returns a formatted message based on the provided template and
     *         provided parameter
     */
    public static String getMessageWithArgs(String pTemplate, Object pArg0, Object pArg1) {
        return MessageFormat.format(pTemplate, new Object[]{
            "" + pArg0,
            "" + pArg1
        });
    }


    /**
     * <p>Returns a formatted message based on the provided template and
     * provided parameter.</p>
     *
     * @param pTemplate the base message
     * @param pArg0     parameter 1
     * @param pArg1     parameter 2
     * @param pArg2     parameter 3
     *
     * @return Returns a formatted message based on the provided template and
     *         provided parameter
     */
    public static String getMessageWithArgs(String pTemplate, Object pArg0, Object pArg1, Object pArg2) {
        return MessageFormat.format(pTemplate, new Object[]{
            "" + pArg0,
            "" + pArg1,
            "" + pArg2
        });
    }


    /**
     * <p>Returns a formatted message based on the provided template and
     * provided parameter.</p>
     *
     * @param pTemplate the base message
     * @param pArg0     parameter 1
     * @param pArg1     parameter 2
     * @param pArg2     parameter 3
     * @param pArg3     parameter 4
     *
     * @return Returns a formatted message based on the provided template and
     *         provided parameter
     */
    public static String getMessageWithArgs(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3) {
        return MessageFormat.format(pTemplate, new Object[]{
            "" + pArg0,
            "" + pArg1,
            "" + pArg2,
            "" + pArg3
        });
    }


    /**
     * <p>Returns a formatted message based on the provided template and
     * provided parameter.</p>
     *
     * @param pTemplate the base message
     * @param pArg0     parameter 1
     * @param pArg1     parameter 2
     * @param pArg2     parameter 3
     * @param pArg3     parameter 4
     * @param pArg4     parameter 5
     *
     * @return Returns a formatted message based on the provided template and
     *         provided parameter
     */
    public static String getMessageWithArgs(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3,
                                            Object pArg4) {
        return MessageFormat.format(pTemplate, new Object[]{
            "" + pArg0,
            "" + pArg1,
            "" + pArg2,
            "" + pArg3,
            "" + pArg4
        });
    }


    /**
     * <p>Returns a formatted message based on the provided template and
     * provided parameter.</p>
     *
     * @param pTemplate the base message
     * @param pArg0     parameter 1
     * @param pArg1     parameter 2
     * @param pArg2     parameter 3
     * @param pArg3     parameter 4
     * @param pArg4     parameter 5
     * @param pArg5     parameter 6
     *
     * @return Returns a formatted message based on the provided template and
     *         provided parameter
     */
    public static String getMessageWithArgs(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3,
                                            Object pArg4, Object pArg5) {
        return MessageFormat.format(pTemplate, new Object[]{
            "" + pArg0,
            "" + pArg1,
            "" + pArg2,
            "" + pArg3,
            "" + pArg4,
            "" + pArg5
        });
    }
}

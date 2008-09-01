/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

/*
 * ConstantPoolInfo.java
 *
 * Created on May 24, 2005, 4:43 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.sun.faces.config.listeners;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.FacesLogger;

/**
 * Taken from the GlassFish V2 source base.
 */
class ConstantPoolInfo {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();
    private static final FacesAnnotationScanner FACES_SCANNER =
          new FacesAnnotationScanner();

    public static final byte CLASS = 7;
    public static final int FIELDREF = 9;
    public static final int METHODREF = 10;
    public static final int STRING = 8;
    public static final int INTEGER = 3;
    public static final int FLOAT = 4;
    public static final int LONG = 5;
    public static final int DOUBLE = 6;
    public static final int INTERFACEMETHODREF = 11;
    public static final int NAMEANDTYPE = 12;
    public static final int ASCIZ = 1;
    public static final int UNICODE = 2;

    byte[] bytes = new byte[Short.MAX_VALUE];


    // ------------------------------------------------------------ Constructors


    /**
     * Creates a new instance of ConstantPoolInfo
     */
    public ConstantPoolInfo() {
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * Read the input channel and initialize instance data structure.
     */
    public boolean containsAnnotation(int constantPoolSize,
                                      final ByteBuffer buffer)
          throws IOException {

        for (int i = 1; i < constantPoolSize; i++) {
            final byte type = buffer.get();
            switch (type) {
                case ASCIZ:
                case UNICODE:
                    final short length = buffer.getShort();
                    if (length < 0 || length > Short.MAX_VALUE) {
                        return true;
                    }
                    buffer.get(bytes, 0, length);
                    /* to speed up the process, I am comparing the first few
                     * bytes to Ljava since all annotations are in the java
                     * package, the reduces dramatically the number or String
                     * construction
                     */
                    if (bytes[0] == 'L' && bytes[1] == 'j' && bytes[2] == 'a') {
                        String stringValue;
                        if (type == ASCIZ) {
                            stringValue =
                                  new String(bytes, 0, length, "US-ASCII");
                        } else {
                            stringValue = new String(bytes, 0, length);
                        }
                        if (FACES_SCANNER.isAnnotation(stringValue)) {
                            return true;
                        }
                    }
                    break;
                case CLASS:
                case STRING:
                    buffer.getShort();
                    break;
                case FIELDREF:
                case METHODREF:
                case INTERFACEMETHODREF:
                case INTEGER:
                case FLOAT:
                    buffer.position(buffer.position() + 4);
                    break;
                case LONG:
                case DOUBLE:
                    buffer.position(buffer.position() + 8);
                    // for long, and double, they use 2 constantPool 
                    i++;
                    break;
                case NAMEANDTYPE:
                    buffer.getShort();
                    buffer.getShort();
                    break;
                default:
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Unknow type constant pool {0} at position {1}",
                                   new Object[]{type, i});
                    }
                    break;
            }
        }
        return false;
    }

}

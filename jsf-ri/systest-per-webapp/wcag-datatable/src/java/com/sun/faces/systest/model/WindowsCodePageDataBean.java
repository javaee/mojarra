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

package com.sun.faces.systest.model;

/**
 *
 * @author edburns
 */
public class WindowsCodePageDataBean {
    
    /** Creates a new instance of WcagTableData */
    public WindowsCodePageDataBean() {
    }
    
    public WindowsCodePageDataBean(String codePageId, String name, boolean acp,
            boolean oemcp, boolean winNT31, boolean winNT351, boolean win95) {
        this.codePageId = codePageId;
        this.name = name;
        this.ACP = acp;
        this.OEMCP = oemcp;
        this.winNT31 = winNT31;
        this.winNT351 = winNT351;
        this.win95 = win95;
    }

    /**
     * Holds value of property codePageId.
     */
    private String codePageId;

    /**
     * Getter for property codePageId.
     * @return Value of property codePageId.
     */
    public String getCodePageId() {
        return this.codePageId;
    }

    /**
     * Setter for property codePageId.
     * @param codePageId New value of property codePageId.
     */
    public void setCodePageId(String codePageId) {
        this.codePageId = codePageId;
    }

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Holds value of property isOEMCP.
     */
    private String isOEMCP;

    /**
     * Holds value of property ACP.
     */
    private boolean ACP;

    /**
     * Getter for property ACP.
     * @return Value of property ACP.
     */
    public boolean isACP() {
        return this.ACP;
    }

    /**
     * Setter for property ACP.
     * @param ACP New value of property ACP.
     */
    public void setACP(boolean ACP) {
        this.ACP = ACP;
    }

    /**
     * Holds value of property OEMCP.
     */
    private boolean OEMCP;

    /**
     * Getter for property OEMCP.
     * @return Value of property OEMCP.
     */
    public boolean isOEMCP() {
        return this.OEMCP;
    }

    /**
     * Setter for property OEMCP.
     * @param OEMCP New value of property OEMCP.
     */
    public void setOEMCP(boolean OEMCP) {
        this.OEMCP = OEMCP;
    }

    /**
     * Holds value of property winNT31.
     */
    private boolean winNT31;

    /**
     * Getter for property winNT31.
     * @return Value of property winNT31.
     */
    public boolean isWinNT31() {
        return this.winNT31;
    }

    /**
     * Setter for property winNT31.
     * @param winNT31 New value of property winNT31.
     */
    public void setWinNT31(boolean winNT31) {
        this.winNT31 = winNT31;
    }

    /**
     * Holds value of property winNT351.
     */
    private boolean winNT351;

    /**
     * Getter for property winNT351.
     * @return Value of property winNT351.
     */
    public boolean isWinNT351() {
        return this.winNT351;
    }

    /**
     * Setter for property winNT351.
     * @param winNT351 New value of property winNT351.
     */
    public void setWinNT351(boolean winNT351) {
        this.winNT351 = winNT351;
    }

    /**
     * Holds value of property win95.
     */
    private boolean win95;

    /**
     * Getter for property win95.
     * @return Value of property win95.
     */
    public boolean isWin95() {
        return this.win95;
    }

    /**
     * Setter for property win95.
     * @param win95 New value of property win95.
     */
    public void setWin95(boolean win95) {
        this.win95 = win95;
    }

    
}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.agnostic.application.navigation;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Backing Bean class that redirects on startSearch() to an other JSF page and
 * tries to transmit the content of the searchTerm property as a URL HTTP-GET
 * parameter.
 *
 * @author deconstruct
 */
@ManagedBean
@ViewScoped
public class OutcomeParameterBean implements Serializable {

    private String searchTermA = "Laurel & Hardy";
    private String searchTermB = "Laurel & Hardy";
    private String searchTermC = "Laurel & Hardy";
    private String searchTermD = "Laurel & Hardy";
    private String searchTermE = "Laurel & Hardy";

    public String startSearchWithUrlEncode() throws UnsupportedEncodingException {
        String queryUrlParameter = java.net.URLEncoder.encode(searchTermA, "UTF-8");
        String redirectTarget = "/outcomeParameterResults.xhtml?query=" + queryUrlParameter + "&otherParameter=someValue&faces-redirect=true";
        return redirectTarget;
    }

    public String startSearchWithoutUrlEncode() throws UnsupportedEncodingException {
        String redirectTarget = "/outcomeParameterResults.xhtml?query=" + searchTermB + "&otherParameter=someValue&faces-redirect=true";
        return redirectTarget;
    }

    public void startSearchViaExternalContext() throws UnsupportedEncodingException, IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String contextPath = ((ServletContext) externalContext.getContext()).getContextPath();
        String redirectTarget = contextPath
                + "/faces/outcomeParameterResults.xhtml?query="
                + java.net.URLEncoder.encode(searchTermC, "UTF-8")
                + "&otherParameter=someValue";

        FacesContext.getCurrentInstance().getExternalContext().redirect(redirectTarget);
    }

    public String getSearchTermA() {
        return searchTermA;
    }

    public void setSearchTermA(String searchTermA) {
        this.searchTermA = searchTermA;
    }

    public String getSearchTermB() {
        return searchTermB;
    }

    public void setSearchTermB(String searchTermB) {
        this.searchTermB = searchTermB;
    }

    public String getSearchTermC() {
        return searchTermC;
    }

    public void setSearchTermC(String searchTermC) {
        this.searchTermC = searchTermC;
    }

    public String getSearchTermD() {
        return searchTermD;
    }

    public void setSearchTermD(String searchTermD) {
        this.searchTermD = searchTermD;
    }

    public String getSearchTermE() {
        return searchTermD;
    }

    public void setSearchTermE(String searchTermE) {
        this.searchTermE = searchTermE;
    }
}

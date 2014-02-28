/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2011 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.systest.model.ajax;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name="ajaxrequest")
@SessionScoped
public class AjaxRequestBean {
    private Integer count = 0;

    private String echo = "echo";
    private String echo1 = "";
    private String echo2 = "";
    private String echo3 = "";
    private String echo4 = "";

    public String getEcho1() {
        return echo1;
    }

    public void setEcho1(String echo1) {
        this.echo1 = echo1;
    }

    public String getEcho2() {
        return echo2;
    }

    public void setEcho2(String echo2) {
        this.echo2 = echo2;
    }

    public String getEcho3() {
        return echo3;
    }

    public void setEcho3(String echo3) {
        this.echo3 = echo3;
    }

    public String getEcho4() {
        return echo4;
    }

    public void setEcho4(String echo4) {
        this.echo4 = echo4;
    }

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }

    public void echoValue(ValueChangeEvent event) {
        String str = (String)event.getNewValue();
        echo = str;
    }

    public void resetEcho(ActionEvent ae) {
        echo = "reset";
        echo1 = "reset";
        echo2 = "reset";
        echo3 = "reset";
        echo4 = "reset";
    }

    public Integer getCount() {
        return count++;
    }

    public void resetCount(ActionEvent ae) {
        count = 0;
    }

    public String contentType = null;
    public String getContentType() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext eContext = context.getExternalContext();
        contentType = eContext.getRequestContentType();
        return contentType;
    }

    public void setcontentType(String contentType) {
        this.contentType = contentType;
    }


}

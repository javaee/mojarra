/*
 * $Id: ModelBean.java,v 1.12 2007/04/27 22:00:43 ofung Exp $
 */

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

package standard;

import java.util.Date;


/**
 * Simple bean for Model value demonstration.
 *
 * @version $Id: ModelBean.java,v 1.12 2007/04/27 22:00:43 ofung Exp $
 */

public class ModelBean {

    //
    // Properties
    //

    protected String label = "Label from Model";


    public String getLabel() {
        return label;
    }


    public void setLabel(String label) {
        this.label = label;
    }


    protected Date date1 = new Date(System.currentTimeMillis());


    public Date getDate1() {
        return date1;
    }


    public void setDate1(Date newDate) {
        date1 = newDate;
    }


    protected Date date2 = new Date(System.currentTimeMillis());


    public Date getDate2() {
        return date2;
    }


    public void setDate2(Date newDate) {
        date2 = newDate;
        System.out.println("date2: " + date2);
    }


    protected Date date3 = new Date(System.currentTimeMillis());


    public Date getDate3() {
        return date3;
    }


    public void setDate3(Date newDate) {
        date3 = newDate;
    }


    protected Date date4 = new Date(System.currentTimeMillis());


    public Date getDate4() {
        return date4;
    }


    public void setDate4(Date newDate) {
        date4 = newDate;
    }


    protected Date time;


    public Date getTime() {
        return time;
    }


    public void setTime(Date newTime) {
        time = newTime;
    }


    protected Date dateTime;


    public Date getDateTime() {
        return dateTime;
    }


    public void setDateTime(Date newDateTime) {
        dateTime = newDateTime;
    }


    protected String hasComponent = null;


    public String getHasComponent() {
        return hasComponent;
    }


    public void setHasComponent(String newHasComponent) {
        hasComponent = newHasComponent;
    }


    protected String whichComponent = null;


    public String getWhichComponent() {
        return whichComponent;
    }


    public void setWhichComponent(String newWhich) {
        whichComponent = newWhich;
    }

    //
    // Actions
    // 

    public String defaultAction() {

        // back end application logic goes here.
        return "success";

    }


    public String postbackAction() {

        // back end application logic goes here.
        return null;

    }


}

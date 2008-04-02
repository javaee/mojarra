/*
 * $Id: ModelBean.java,v 1.7 2004/05/12 18:47:06 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package standard;

import java.util.Date;


/**
 * Simple bean for Model value demonstration.
 *
 * @version $Id: ModelBean.java,v 1.7 2004/05/12 18:47:06 ofung Exp $
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

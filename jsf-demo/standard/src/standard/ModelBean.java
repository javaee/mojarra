/*
 * $Id: ModelBean.java,v 1.5 2003/11/11 05:26:16 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;

import java.util.Date;


/**
 * Simple bean for Model value demonstration. 
 *
 * @version $Id: ModelBean.java,v 1.5 2003/11/11 05:26:16 rkitain Exp $
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

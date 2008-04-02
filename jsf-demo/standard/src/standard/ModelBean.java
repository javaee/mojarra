/*
 * $Id: ModelBean.java,v 1.3 2003/08/25 21:25:17 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;

import javax.faces.application.Action;

import java.util.Date;


/**
 * Simple bean for Model value demonstration. 
 *
 * @version $Id: ModelBean.java,v 1.3 2003/08/25 21:25:17 eburns Exp $
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
    
    protected Date date1;
    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date newDate) {
        date1 = newDate;
    }

    protected Date date2;
    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date newDate) {
        date2 = newDate;
	System.out.println("date2: " + date2);
    }

    protected Date date3;
    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date newDate) {
        date3 = newDate;
    }

    protected Date date4;
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

    public Action getDefaultAction() {
	return new Action() {
		public String invoke() {
		    // back end application logic goes here.
		    return "success";
		}
	    };
    }

    public Action getPostbackAction() {
	return new Action() {
		public String invoke() {
		    // back end application logic goes here.
		    return null;
		}
	    };
    }

}

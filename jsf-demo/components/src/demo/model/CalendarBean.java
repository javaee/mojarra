/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
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

package demo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Locale;
import java.text.MessageFormat;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import components.components.CalendarComponent;

/**
 * <p>Backing file for the Calendar demo.</p>
 * The model simply consists in a Date object, and we
 * support actions to "process" the date (when submit button pushed)
 * as well as set the locale.</p>
 */

public class CalendarBean implements Serializable {
    
    //**************************************************************************
    // Constructor
    
    /**
     * We initialize our model with today's date.
     */
    public CalendarBean() {
        this.date = new Date();
    }

    //**************************************************************************
    // Component binding
    private transient CalendarComponent calendar = null;
    public CalendarComponent getCalendar() { return calendar; }
    public void setCalendar(CalendarComponent calendar)
    { this.calendar = calendar; }

    //**************************************************************************
    // Model processing
        
    // The model is simply a Date object
    private Date date = null;
    public Date getDate() { return date; }    
    public void setDate(Date date) { this.date = date; } 

    //**************************************************************************
    // Action processing
    
    public String selectLocaleEN() {
        setLocale("en");
        return null;
    }
    
    public String selectLocaleFR() {
        setLocale("fr");
        return null;
    }
    
    /**
     * <p>Process the date.</p>
     */
    public String process() {
        String message;        
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	ResourceBundle rb = 
        ResourceBundle.getBundle("demo.model.Resources", locale);  
        
	message = (String)rb.getString("calendar.dateProcessed");
        MessageFormat mf = new MessageFormat("");
        mf.setLocale(locale);
        mf.applyPattern(message);
	message = mf.format(new Object[] {date});
        
        append(message); 
        
	return (null);
    }

    //**************************************************************************
    // Utility methods
    
    private void setLocale(String locale) {
        Date date = getDate();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale(locale, ""));
        setDate(date);
    }

    /**
     * <p>Append an informational message to the set of messages that will
     * be rendered when this view is redisplayed.</p>
     *
     * @param message Message text to be added
     */
    private void append(String message) {
        FacesContext.getCurrentInstance().addMessage(
        null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }
}

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

package demo.model;

import components.components.CalendarComponent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

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


    public CalendarComponent getCalendar() {
        return calendar;
    }


    public void setCalendar(CalendarComponent calendar) {
        this.calendar = calendar;
    }

    //**************************************************************************
    // Model processing
        
    // The model is simply a Date object
    private Date date = null;


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

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
        Locale locale = FacesContext.getCurrentInstance().getViewRoot()
            .getLocale();
        ResourceBundle rb =
            ResourceBundle.getBundle("demo.model.Resources", locale);

        message = (String) rb.getString("calendar.dateProcessed");
        MessageFormat mf = new MessageFormat("");
        mf.setLocale(locale);
        mf.applyPattern(message);
        message = mf.format(new Object[]{date});

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
        FacesContext.getCurrentInstance().addMessage(null,
                                                     new FacesMessage(
                                                         FacesMessage.SEVERITY_INFO,
                                                         message, null));
    }
}

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

package components.components;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * <p>CalendarComponent is a JavaServer Faces component that is essentially
 * a UIInput component specialized for dates and augmented
 * with a graphical representation of a calendar to ease the selection of
 * a specific date.<p>
 */

public class CalendarComponent extends UIComponentBase {
    
    // FIXME: eventually, we'll have more properties here...
    
    /**
     * <p>Return the component family for this component.</p>
     */
    public String getFamily() {

        return ("Calendar");

    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        sb.append("CalendarComponent ");
        sb.append("(").append(getId()).append(")\n");
        //sb.append("(").append(getClientId(facesContext)).append(")\n");
        Map map = getAttributes();
        Set attrSet = map.keySet();
        Iterator iter = attrSet.iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            Object value = map.get(key);
            sb.append(key).append(": ").append(value).append("\n");
        }
        return sb.toString();
    }
}

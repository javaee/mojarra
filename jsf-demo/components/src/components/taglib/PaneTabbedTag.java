/*
 * $Id: PaneTabbedTag.java,v 1.6 2003/12/17 15:19:15 rkitain Exp $
 */

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

package components.taglib;


import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class creates a <code>PaneComponent</code> instance
 * that represents a the overall tabbed pane control.
 */
public class PaneTabbedTag extends UIComponentTag {


    private static Log log = LogFactory.getLog(PaneTabbedTag.class);


    private String contentClass = null;
    public void setContentClass(String contentClass) {
        this.contentClass = contentClass;
    }


    private String paneClass = null;
    public void setPaneClass(String paneClass) {
        this.paneClass = paneClass;
    }


    private String selectedClass = null;
    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }


    private String unselectedClass = null;
    public void setUnselectedClass(String unselectedClass) {
        this.unselectedClass = unselectedClass;
    }


    public String getComponentType() {
        return ("Pane");
    }


    public String getRendererType() {
        return ("Tabbed");
    }


    public void release() {
        super.release();
        contentClass = null;
        paneClass = null;
        selectedClass = null;
        unselectedClass = null;
    }


    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        if (contentClass != null) {
            if (isValueReference(contentClass)) {
                ValueBinding vb =
                    context.getApplication().createValueBinding(contentClass);
                component.setValueBinding("contentClass", vb);
            } else {
                component.getAttributes().put("contentClass", contentClass);
            }
        }

        if (paneClass != null) {
            if (isValueReference(paneClass)) {
                ValueBinding vb =
                    context.getApplication().createValueBinding(paneClass);
                component.setValueBinding("paneClass", vb);
            } else {
                component.getAttributes().put("paneClass", paneClass);
            }
        }

        if (selectedClass != null) {
            if (isValueReference(selectedClass)) {
                ValueBinding vb =
                    context.getApplication().createValueBinding(selectedClass);
                component.setValueBinding("selectedClass", vb);
            } else {
                component.getAttributes().put("selectedClass", selectedClass);
            }
        }

        if (unselectedClass != null) {
            if (isValueReference(unselectedClass)) {
                ValueBinding vb =
                    context.getApplication().createValueBinding(unselectedClass);
                component.setValueBinding("unselectedClass", vb);
            } else {
                component.getAttributes().put("unselectedClass", unselectedClass);
            }
        }
    }


}

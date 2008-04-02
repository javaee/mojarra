/*
 * $Id: PaneTabLabelTag.java,v 1.6 2003/12/17 15:19:15 rkitain Exp $
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
 * that represents a tab button control on the tab pane.
 */
public class PaneTabLabelTag extends UIComponentTag {

    private static Log log = LogFactory.getLog(PaneTabLabelTag.class);


    private String commandName = null;
    public void setCommandName(String newCommandName) {
        commandName = newCommandName;
    }


    private String image = null;
    public void setImage(String newImage) {
        image = newImage;
    }


    private String label = null;
    public void setLabel(String newLabel) {
        label = newLabel;
    }


    public String getComponentType() {
        return ("Pane");
    }


    public String getRendererType() {
        return ("TabLabel");
    }


    public void release() {
        super.release();
        this.commandName = null;
        this.image = null;
        this.label = null;
    }


    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        if (commandName != null) {
            if (isValueReference(commandName)) {
                ValueBinding vb =
                    context.getApplication().createValueBinding(commandName);
                component.setValueBinding("commandName", vb);
            } else {
                component.getAttributes().put("commandName", commandName);
            }
        }

        if (image != null) {
            if (isValueReference(image)) {
                ValueBinding vb =
                    context.getApplication().createValueBinding(image);
                component.setValueBinding("image", vb);
            } else {
                component.getAttributes().put("image", image);
            }
        }

        if (label != null) {
            if (isValueReference(label)) {
                ValueBinding vb =
                    context.getApplication().createValueBinding(label);
                component.setValueBinding("label", vb);
            } else {
                component.getAttributes().put("label", label);
            }
        }
    }


}

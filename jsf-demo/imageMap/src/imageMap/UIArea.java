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

package imageMap;


import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.context.ResponseWriter;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * This class represents the <code>UIArea</code> component, which corresponds
 * to the <code>area</code> tag.  An <code>area</code> tag specifies the
 * geometric regions of an image map. 
*/

public class UIArea extends UIComponentBase {

    // Component type for this component
    public static final String TYPE = "Area";

    // Return our component type
    public String getComponentType() {
        return (TYPE);
    }

    // Renders the <code>area</code> tags
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
	System.out.println("Area: context is null");
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }

	ImageArea ia = (ImageArea) context.getHttpSession().getAttribute(this.getModelReference());
        if ( ia == null) {
            System.out.println("ImageArea bean is null");
            return;  
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<area shape=\"");
        writer.write(ia.getShape());
	writer.write("\"" );
	writer.write(" coords=\"");
	writer.write(ia.getCoords());
	writer.write("\" onclick=\"document.forms[0].selectedArea.value='");
	writer.write(getComponentId());
	writer.write("'; document.forms[0].submit()\"");
	writer.write(" onmouseover=\"");
	writer.write("document.forms[0].mapImage.src='");
	writer.write((String) getAttribute("onmouseover"));
	writer.write("';\"");
	writer.write(" onmouseout=\"");
	writer.write("document.forms[0].mapImage.src='");
	writer.write((String) getAttribute("onmouseout"));
	writer.write("';\"");
	writer.write(" alt=\"");
	writer.write(ia.getAlt());
	writer.write("\"");
        writer.write("\">");

    }

    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        if ((event == null) || (phaseId == null)) {
            throw new NullPointerException();
        }
        return false;
    } 
  
}

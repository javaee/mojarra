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


package components.model;


import components.components.AreaSelectedEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


/**
 * ImageMap is the "backing file" class for the image map application.
 * It contains a method event handler that sets the locale from
 * information in the <code>AreaSelectedEvent</code> event.
 */
public class ImageMap {

    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The locales to be selected for each hotspot, keyed by the
     * alternate text for that area.</p>
     */
    private Map locales = null;

    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct a new instance of this image map.</p>
     */
    public ImageMap( ) {
        locales = new HashMap();
        locales.put("NAmerica", Locale.ENGLISH);
        locales.put("SAmerica", new Locale("es","es"));
        locales.put("Germany", Locale.GERMAN);
        locales.put("Finland", new Locale("fi","fi"));
        locales.put("France", Locale.FRENCH);
    }


    /**
     * <p>Select a new Locale based on this event.</p>
     *
     * @param event The {@link AreaSelectedEvent} that has occurred
     */
    public void processAreaSelected(ActionEvent actionEvent) {
	AreaSelectedEvent event = (AreaSelectedEvent) actionEvent;
        String current = event.getMapComponent().getCurrent();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale((Locale) locales.get(current));
    }

    /**
     * <p>Return an indication for navigation.  Application using this component,
     * can refer to this method via an <code>action</code> expression in their
     * page, and set up the "outcome" (success) in their navigation rule.
     */
    public String status() {
        return "success";
    }
}

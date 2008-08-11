/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.FacesLogger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 *
 * @author edburns
 */
public class CompositeFacetRenderer extends Renderer {
    
    // Log instance for this class
    protected static final Logger logger = FacesLogger.RENDERKIT.getLogger();


    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
       String facetName = (String)
               component.getAttributes().get(UIComponent.FACETS_KEY);
       if (null == facetName) {
           throw new IOException("PENDING_I18N Unable to find facet name to insert facet into composite component");
       }
       
       UIComponent currentCompositeComponent = component.getCurrentCompositeComponent();
       if (null != currentCompositeComponent) {
           UIComponent facet = currentCompositeComponent.getFacet(facetName);
           if (null != facet) {
               facet.encodeAll(context);
           }
           else {
               if (logger.isLoggable(Level.FINE)) {
                   logger.log(Level.FINE,
                           "Could not find facet named {0}",
                           facetName);
               }
           }
       }       
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }


    

}

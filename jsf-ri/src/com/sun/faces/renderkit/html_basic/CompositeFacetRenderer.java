/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 *
 * @author edburns
 */
public class CompositeFacetRenderer extends Renderer {

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
           UIComponent facet = null;
           if (null == (facet = currentCompositeComponent.getFacet(facetName))){
               throw new IOException("PENDING_I18N Unable to find facet to insert facet into composite component");
           }
           facet.encodeAll(context);
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

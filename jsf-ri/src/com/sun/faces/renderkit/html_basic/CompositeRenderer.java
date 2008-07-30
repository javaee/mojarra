/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 *
 * @author edburns
 */
public class CompositeRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        Map<String,UIComponent> facets = component.getFacets();
        UIComponent compositeRoot = facets.get(UIComponent.COMPOSITE_FACET_NAME);
        if (null == compositeRoot) {
            throw new IOException("PENDING_I18N: Unable to find composite " + 
                    " component root for composite component with id " + 
                    component.getId() + " and class " + 
                    component.getClass().getName());
        }
        compositeRoot.encodeAll(context);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }


    

}

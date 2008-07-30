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
public class ConsumingPageCompositeChildrenRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
       UIComponent currentCompositeComponent = component.getCurrentCompositeComponent();
       if (null != currentCompositeComponent) {

           // It must be true that each of the children of the composite 
           // component must come from the consuming page, and therefore
           // are to be rendered right now.
           List<UIComponent> children = currentCompositeComponent.getChildren();
           for (UIComponent cur : children) {
               cur.encodeAll(context);
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

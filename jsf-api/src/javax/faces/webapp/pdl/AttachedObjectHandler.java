/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.webapp.pdl;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author edburns
 */
public interface AttachedObjectHandler {
    
    public void applyAttachedObject(FacesContext context, UIComponent parent);
    
    public String getFor();

}

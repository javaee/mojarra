/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.webapp.pdl;

import java.util.List;
import javax.faces.component.UIComponent;

/**
 *
 * @author edburns
 */
public interface AttachedObjectTarget {
    
    public List<UIComponent> getTargets();
    
    public static final String ATTACHED_OBJECT_TARGETS_KEY =
            "javax.faces.webapp.pdl.AttachedObjectTargets";
    
    public String getName();
           

}

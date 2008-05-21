/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package component;

import javax.faces.application.Resource;
import javax.faces.component.CompositeComponent;
import javax.faces.component.UINamingContainer;

/**
 *
 * @author edburns
 */
public class SimpleCompositeComponent extends UINamingContainer implements CompositeComponent {

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    private Resource resource = null;

    @Override
    public String getFamily() {
        return "SimpleCompositeComponent";
    }
    
}

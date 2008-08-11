/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ezcomp;

import javax.faces.component.UIComponentBase;

/**
 *
 * @author edburns
 */
public class componentWithBackingJavaClass extends UIComponentBase {

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer";
    }
    
    

}

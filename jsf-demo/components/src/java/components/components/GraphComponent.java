/*
 * $Id: GraphComponent.java,v 1.2 2005/05/17 17:00:15 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
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

package components.components;


import components.model.Graph;
import components.model.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.faces.FactoryFinder;

/**
 * Component wrapping a {@link Graph} object that is pointed at by the
 * a value binding reference expression.  This component supports
 * the processing of a {@link ActionEvent} that will toggle the expanded
 * state of the specified {@link Node} in the {@link Graph}.
 */

public class GraphComponent extends UICommand {

    private static Log log = LogFactory.getLog(GraphComponent.class);


    public GraphComponent() {

        // set a default actionListener to expand or collapse a node
        // when a node is clicked.
        Class signature[] = {ActionEvent.class};
        // add listener only if its an initial request. If its a postback
        // the listener will be persisted by the state saving mechanism, so
        // we don't want to accumulate the listeners.
        FacesContext context = FacesContext.getCurrentInstance();
        String renderkitId = 
                context.getApplication().getViewHandler().
                calculateRenderKitId(context);
        ResponseStateManager rsm = this.getResponseStateManager(context,
                renderkitId);
        if (!rsm.isPostback(context)) {
            setActionListener(FacesContext.getCurrentInstance().getApplication()
                          .createMethodBinding(
                              "#{GraphBean.processGraphEvent}", signature));
        }

    }


    /**
     * <p>Return the component family for this component.</p>
     */
    public String getFamily() {

        return ("Graph");

    }
    
    protected ResponseStateManager getResponseStateManager(FacesContext context, 
            String renderKitId){

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

        RenderKit renderKit = renderKitFactory.getRenderKit(context, renderKitId);        

        if ( renderKit != null) {
            return renderKit.getResponseStateManager();
        }
        return null;
    }

}

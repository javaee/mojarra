package com.sun.faces.sandbox.handler;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/*******************************************************************************
 * <p>
 * This navigation handler will defer to the default navigation handler unless
 * the action outcome starts with a forward slash ("/"). If the outcome does
 * start with a forward slash, then the resulting view-id that is forwarded to
 * will just be the outcome suffixed with the JSF default suffix. E.g. - An
 * outcome of "/home" would resolve to "/home.jsp" or "/home.xhtml" (for
 * facelets). An outcome of "home" would use the navigation rules defined in
 * faces-confix.xml to resolve.
 * </p>
 * 
 * <p>
 * Configuration:
 * </p>
 * 
 * <pre>
 *  &lt;application&gt;
 *     ...
 *     &lt;navigation-handler&gt;com.iecokc.faces.handlers.navigation.ViewIdIsOutcomeNavigationHandler&lt;/navigation-handler&gt;
 *  &lt;/application&gt;
 * </pre>
 * 
 * @author Blevins
 * 
 */
public class ViewIdIsOutcomeNavigationHandler extends NavigationHandler {
    private NavigationHandler base;

    private String defaultSuffix;

    public ViewIdIsOutcomeNavigationHandler(NavigationHandler base) {
        super();
        this.base = base;
    }

    /***************************************************************************
     * <p>
     * Handle the navigation request implied by the specified parameters.
     * </p>
     * <p>
     * Outcomes starting with a forward slash are directly converted into a
     * view-id.
     * </p>
     * 
     * @param context
     *            <code>FacesContext</code> for the current request
     * @param fromAction
     *            The action binding expression that was evaluated to retrieve
     *            the specified outcome (if any)
     * @param outcome
     *            The logical outcome returned by the specified action
     * 
     * @exception IllegalArgumentException
     *                if the configuration information for a previously saved
     *                position cannot be found
     * @exception IllegalArgumentException
     *                if an unknown State type is found
     */
    @Override
    public void handleNavigation(FacesContext context, String fromAction,
            String outcome) {

        // Get the default prefix
        if (defaultSuffix == null) {
            defaultSuffix = context.getExternalContext().getInitParameter(
                    ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
        }

        // Handle outcomes that start with a forward slash
        if (outcome != null && outcome.startsWith("/")) {
            String viewId = outcome + defaultSuffix;
            UIViewRoot view = context.getApplication().getViewHandler()
                    .createView(context, viewId);
            view.setViewId(viewId);
            context.setViewRoot(view);
            return;
        }

        // Handle using default mechanisms
        this.base.handleNavigation(context, fromAction, outcome);
    }

}

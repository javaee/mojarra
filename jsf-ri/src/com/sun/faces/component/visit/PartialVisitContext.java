/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.component.visit;

import javax.faces.component.visit.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;

/**
 *
 * <p class="changed_added_2_0">A VisitContext implementation that is
 * used when performing a partial component tree visit.</p>
 *
 * RELEASE_PENDING
 * @since 2.0
 */
public class PartialVisitContext extends VisitContext {

    /**
     * Creates a PartialVisitorContext instance.
     * @param facesContext the FacesContext for the current request
     * @param clientIds the client ids of the components to visit
     * @throws NullPointerException  if {@code facesContext}
     *                               is {@code null}
     */    
    public PartialVisitContext(FacesContext facesContext,
                               Collection<String> clientIds) {
        this(facesContext, clientIds, null);
    }

    /**
     * Creates a PartialVisitorContext instance with the specified hints.
     * @param facesContext the FacesContext for the current request
     * @param clientIds the client ids of the components to visit
     * @param hints a the VisitHints for this visit
     * @throws NullPointerException  if {@code facesContext}
     *                               is {@code null}
     */    
    public PartialVisitContext(FacesContext facesContext,
                               Collection<String> clientIds,
                               Set<VisitHint> hints) {
        if (facesContext == null) {
            throw new NullPointerException();
        }

        this.facesContext = facesContext;

        // Copy the client ids into a HashSet to allow for quick lookups.
        this.clientIds = (clientIds == null) ? 
                           new HashSet<String>() :
                           new HashSet<String>(clientIds);
  
        // Make another copy so we can easily track unvisited ids
        this.unvisitedClientIds = new HashSet<String>(this.clientIds);
  
        // Populate a collection of the ids (not client ids - just plain
        // old ids) that we want to visit.  This allows us to avoid extra
        // UIComponent.getClientId() calls in PartialVisitContext.visit().
        this.ids = populateIds(this.clientIds);

        // Populate nested client ids needed by getIdsToVisit()
        this.nestedClientIds = populateNestedClientIds(this.clientIds);

        // Copy and store hints - ensure unmodifiable and non-empty
        EnumSet<VisitHint> hintsEnumSet = ((hints == null) || (hints.isEmpty()))
                                          ? EnumSet.noneOf(VisitHint.class)
                                          : EnumSet.copyOf(hints);

        this.hints = Collections.unmodifiableSet(hintsEnumSet);
    }

    /**
     * @see VisitContext#getFacesContext VisitContext.getFacesContext()
     */
    @Override
    public FacesContext getFacesContext() {
        return facesContext;
    }

    /**
     * @see VisitContext#getHints VisitContext.getHints
     */
    @Override
    public Set<VisitHint> getHints() {
        return hints;
    }

    /**
     * @see VisitContext#getIdsToVisit VisitContext.getIdsToVisit()
     */
    @Override
    public Collection<String> getIdsToVisit(UIComponent component) {

        // Make sure component is a NamingContainer
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
        }

        String clientId = component.getClientId();
        Collection<String> ids = nestedClientIds.get(clientId);

        if (ids == null)
          return Collections.emptyList();

        return Collections.unmodifiableCollection(ids);     
    }

    /**
     * @see VisitContext#invokeVisitCallback VisitContext.invokeVisitCallback()
     */
    @Override
    public VisitResult invokeVisitCallback(UIComponent component, 
                                           VisitCallback callback) {

        // First sure that we should visit this component - ie.
        // that this component is represented in our id set.
        String clientId = getVisitId(component);

        if (clientId == null) {
            // Not visiting this component, but allow visit to
            // continue into this subtree in case we've got
            // visit targets there.
            return VisitResult.ACCEPT;
        }

        // If we made it this far, the component matches one of
        // client ids, so perform the visit.
        VisitResult result = callback.visit(this, component);

        // Remove the component from our "unvisited" collection
        unvisitedClientIds.remove(clientId);

        // If the unvisited collection is now empty, we are done.
        // Return VisitResult.COMPLETE to terminate the visit.
        if (unvisitedClientIds.isEmpty())
            return VisitResult.COMPLETE;

        // Otherwise, just return the callback's result 
        return result;
    }

    // Tests whether the specified component should be visited.
    // If so, returns its client id.  If not, returns null.
    private String getVisitId(UIComponent component) {

        // We first check to see whether the component's id
        // is in our id collection.  We do this before checking
        // for the full client id because getting the full client id
        // is more expensive than just getting the local id.
        String id = component.getId();
        if ((id != null) && !ids.contains(id))
            return null;

        // The id was a match - now check the client id.
        // note that client id should never be null (should be
        // generated even if id is null, so asserting this.)
        String clientId = component.getClientId();
        assert(clientId != null);

        return clientIds.contains(clientId) ? clientId : null;
    }

    // Give a collection of client ids, return a collection of plain
    // old ids
    private Collection<String> populateIds(Collection<String> clientIds)
    {
        FacesContext facesContext = getFacesContext();
        char separator = UINamingContainer.getSeparatorChar(facesContext);
        HashSet<String> ids = new HashSet<String>(clientIds.size());

        for (String clientId : clientIds)
        {
            int lastIndex = clientId.lastIndexOf(separator);

            String id = null;

            if (lastIndex < 0) {
                id = clientId;
            } else if (lastIndex < (clientId.length() - 1)) {
                id = clientId.substring(lastIndex + 1);              
            }
 
            if (id != null)
              ids.add(id);
        }

        return ids;
    }

    // Populates the map that tracks nested ids underneath naming containers
    private Map<String,Collection<String>> populateNestedClientIds(Collection<String> clientIds) {

        FacesContext facesContext = getFacesContext();
        char separator = UINamingContainer.getSeparatorChar(facesContext);

        Map<String,Collection<String>> nestedClientIds = 
            new HashMap<String,Collection<String>>();

        for (String clientId : clientIds)
            populateNestedClientId(nestedClientIds, clientId, separator);

        return nestedClientIds;
    }

    // Given a single client id, populate the map with all possible
    // nested client ids
    private void populateNestedClientId(Map<String, Collection<String>> nestedClientIds,
                                        String clientId,
                                        char separator) {


        // Loop over the client id and find the substring corresponding to
        // each ancestor NamingContainer client id.  For each ancestor
        // NamingContainer, add an entry into the map for the full client
        // id.

        int length = clientId.length();

        for (int i = 0; i < length; i++) {

            if (clientId.charAt(i) == separator) {

                // We found an ancestor NamingContainer client id - add 
                // an entry to the map.
                String namingContainerClientId = clientId.substring(0, i);

                // Check to see whether we've already ids under this
                // NamingContainer client id.  If not, create the 
                // Collection for this NamingContainer client id and
                // stash it away in our map
                Collection<String> c = nestedClientIds.get(namingContainerClientId);

                if (c == null) {
                    // TODO: smarter initial size?
                    c = new ArrayList<String>();
                    nestedClientIds.put(namingContainerClientId, c);
                }

                // Stash away the client id
                c.add(clientId);
            }
        }
    }


    // The client ids to visit
    private Collection<String> clientIds;

    // The ids to visit
    private Collection<String> ids;

    // The client ids that have yet to be visited
    private Collection<String> unvisitedClientIds;

    // This map contains the information needed by getIdsToVisit().
    // The keys in this map are NamingContainer client ids.  The values
    // are collections containing all of the client ids to visit within
    // corresponding naming container.
    private Map<String,Collection<String>> nestedClientIds;

    // The FacesContext for this request
    private FacesContext facesContext;

    // Our visit hints
    private Set<VisitHint> hints;
}

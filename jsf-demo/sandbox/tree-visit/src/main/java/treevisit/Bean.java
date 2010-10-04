/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package treevisit;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * <p>This bean has the methods that are used to illustrate
 *  the tree visitor API</p>
 *
 */

@ManagedBean(name="treevisit")
@SessionScoped
public class Bean {

    //
    // Instance Variables
    //

    // List of SelectItems representing all client ids on the page
    private List<SelectItem> items;

    // List of ids selected by the end user
    private List<String> selectedIds;

    // List of ids visited by our partial tree visit.
    private List<String> visitedIds;

    // Just a silly data model that we can use for testing
    // UIData tree visiting.
    private List<RowData> testData;

    // Flag indicating whether or not we should visit rendered
    // subtrees only.
    private boolean visitRendered;

    //
    // Constructors
    //
    
    public Bean() {
    }

    // Returns a list containing a SelectItem corresponding to
    // every component client id in this component tree.
    public List<SelectItem> getItems() {

        if (items == null)
            items = loadItems();

        return items;
    }

    // Returns the list of ids to visit
    public List<String> getSelectedIds() {
        return selectedIds;
    }

    // Sets the list of ids to visit
    public void setSelectedIds(List<String> selectedIds) {
        this.selectedIds = selectedIds;
    }

    // Returns the list of ids that were visited
    public List<String> getVisitedIds() {
        return visitedIds;
    }

    // Returns test data for UIData tree visit testing.
    public List<RowData> getTestData() {
        if (testData == null)
            testData = loadTestData();

        return testData;
    }

    // Returns whether we should visit rendered components only
    public boolean getVisitRendered() {
        return visitRendered;
    }

    // Sets whether we shoudl visit rendered components only
    public void setVisitRendered(boolean visitRendered) {
        this.visitRendered = visitRendered;
    }


    // ActionListener that triggers a partial visit of the
    // selected ids.
    public void visit(ActionEvent ae) {

        // To perform a tree visit, we need three things:
        // 1. A VisitContext
        // 2. A VisitCallback
        // 3. A component at which to start the visit.

        // Get our VisitContext.  We use PartialVisitContext since we
        // we only want to visit a subset of the component tree.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        EnumSet<VisitHint> hints = getVisitRendered() ?
            EnumSet.of(VisitHint.SKIP_UNRENDERED) : null;

        // Get ths ids to visit.  Note that this must always be
        // non-null - otherwise we do a full visit.
        java.util.Collection<String> idsToVisit = getSelectedIds();
        if (idsToVisit == null)
            idsToVisit = Collections.emptyList();

        VisitContext visitContext = 
            VisitContext.createVisitContext(facesContext, idsToVisit, hints);

        // Use CollectIdsVisitCallback to collect up visited ids
        CollectIdsVisitCallback visitCallback = new CollectIdsVisitCallback();

        // Start the visit at the view root
        UIComponent viewRoot = facesContext.getViewRoot();

        // Do the visit
        viewRoot.visitTree(visitContext, visitCallback);

        // And stash away the results
        visitedIds = visitCallback.getVisitedIds();
    }

    // Loads the items - ie. the list of client ids for
    // all components in this document.
    private List<SelectItem> loadItems() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        assert(facesContext != null);

        // We collect ids by performing a full tree visit.  So, we
        // need a VisitContext, a VisitCallback, and a component to
        // initiate the visit on.
        VisitContext visitContext = VisitContext.createVisitContext(facesContext);
        CollectIdsVisitCallback visitCallback = new CollectIdsVisitCallback();

        // Visit the subtree under the "mainGrid" component to populate
        // the visitable items list.
        UIComponent viewRoot = facesContext.getViewRoot();
        UIComponent mainGrid = viewRoot.findComponent("form:mainGrid");

        mainGrid.visitTree(visitContext, visitCallback);

        List<String> ids = visitCallback.getVisitedIds();
        List<SelectItem> selectItems = new ArrayList<SelectItem>(ids.size());

        for (String id : ids) {
            selectItems.add(new SelectItem(id));
        }

        return selectItems;
    }

    // Load data that we use to test UIData tree visiting.
    private List<RowData> loadTestData() {
      List<RowData> data = new ArrayList<RowData>(5);

      for (int i = 0; i < 5; i++) {
          RowData rowData = new RowData("Row " + i);
          data.add(rowData);
      }

      return data;
    }

    // Callback class for collecting all ids
    static private class CollectIdsVisitCallback implements VisitCallback {

        // Collect ids in the list
        private List<String> ids = new ArrayList<String>();

        // Performs the visit
        public VisitResult visit(VisitContext context, UIComponent component) {

            String clientId = component.getClientId();

            // For the moment let's blow off any top-level auto-generated
            // ids.  Facelets generates several UIInstructions components
            // which seem to be removed from the component tree at some point.
            // These might cause some confusion, so excluding this for our list.
            if (!clientId.startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
                ids.add(clientId);

            return VisitResult.ACCEPT;
        }

        // Returns the collected ids
        public List<String> getVisitedIds() {
            return ids;
        }
    }

    // A silly little class that we use to pouplate a DataModel so
    // that we can test UIData.treeVisit().
    static public class RowData {

        public RowData(String label) {
            this.label = label;
        }

        // Returns the label
        public String getLabel() {
            return label;
        }

        // Returns a count for the number of times this
        // row has been updated.
        public int getCount() {
            return count;
        }

        // Increments the count
        public void increment(ActionEvent ae) {
            count++;
        }

        private String label;
        private int count;
    }
}

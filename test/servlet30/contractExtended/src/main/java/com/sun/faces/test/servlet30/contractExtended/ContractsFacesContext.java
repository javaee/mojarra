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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.test.servlet30.contractExtended;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.event.PhaseId;

/**
 * Wrap FacesContext to allow general contracts activation through a managed
 * bean.
 *
 * @author dueni
 *
 */
public class ContractsFacesContext extends FacesContextWrapper {

    private static final Logger LOG = Logger.getLogger(ContractsFacesContext.class.getName());

    private static final String ACTIVE_CONTRACTS = "active-contracts";

    private FacesContext wrapped;

    private boolean activeContractsEvaluated = false;

    public ContractsFacesContext(FacesContext toWrap) {
        wrapped = toWrap;
    }

    @Override
    public FacesContext getWrapped() {
        return wrapped;
    }

    @Override
    public List<String> getResourceLibraryContracts() {
        if (!activeContractsEvaluated) {
            activeContractsEvaluated = true;
            String value = getExternalContext().getInitParameter(ACTIVE_CONTRACTS);
            if (value != null) {
                try {
                    ELContext el = getELContext();
                    ExpressionFactory elFactory = getApplication().getExpressionFactory();
                    ValueExpression ve = elFactory.createValueExpression(el, value, Object.class);
                    Object result = ve.getValue(el);
                    if (result instanceof String && !((String) result).isEmpty()) {
                        String[] contracts = ((String) result).split(",");
                        getWrapped().setResourceLibraryContracts(Arrays.asList(contracts));
                    }
                } catch (ELException elx) {
                    LOG.log(Level.SEVERE, "Exception while evaluating '" + ACTIVE_CONTRACTS
                            + "' web.xml context-parameter!", elx);
                }
            }
        }
        return getWrapped().getResourceLibraryContracts();
    }

    @Override
    public void setCurrentPhaseId(PhaseId currentPhaseId) {
        if (currentPhaseId == PhaseId.RENDER_RESPONSE) {
            activeContractsEvaluated = false;
        }
        super.setCurrentPhaseId(currentPhaseId);
    }
}

/*
 * $Id: TestActionListener.java,v 1.3 2003/02/20 22:46:48 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;


/**
 * <p>Test implementation of {@link ActionListener}.</p>
 */

public class TestActionListener implements ActionListener {


    public TestActionListener(PhaseId phaseId) {
        this.phaseId = phaseId;
    }

    private int count = 0;

    public int getCount() {
        return (this.count);
    }

    private PhaseId phaseId = null;

    public PhaseId getPhaseId() {
        return (this.phaseId);
    }

    public void processAction(ActionEvent event) {
        count++;
    }


}

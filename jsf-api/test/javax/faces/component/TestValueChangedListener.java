/*
 * $Id: TestValueChangedListener.java,v 1.2 2003/01/16 20:24:24 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.event.PhaseId;


/**
 * <p>Test implementation of {@link ValueChangedListener}.</p>
 */

public class TestValueChangedListener implements ValueChangedListener {


    public TestValueChangedListener(PhaseId phaseId) {
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

    public void processValueChanged(ValueChangedEvent event) {
        count++;
    }


}

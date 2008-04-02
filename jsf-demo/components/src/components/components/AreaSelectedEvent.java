/*
 * $Id: AreaSelectedEvent.java,v 1.1 2003/08/26 18:40:43 craigmcc Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
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


import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;


/**
 * <p>A {@link FacesEvent} indicating that the specified {@link AreaComponent}
 * has just become the currently selected hotspot within the source
 * {@link MapComponent}.</p>
 */

public class AreaSelectedEvent extends FacesEvent {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link AreaSelectedEvent} from the specified
     * source map, selecting the specified area.</p>
     *
     * @param map The {@link MapComponent} originating this event
     * @param area The {@link AreaComponent} that was selected (may be null)
     */
    public AreaSelectedEvent(MapComponent map, AreaComponent area) {
        super(map);
        this.areaComponent = area;
    }


    // -------------------------------------------------------------- Properties


    private AreaComponent areaComponent = null;


    /**
     * <p>Return the {@link AreaComponent} of the selected area.</p>
     */
    public AreaComponent getAreaComponent() {
        return (this.areaComponent);
    }


    /**
     * <p>Return the {@link MapComponent} of the map for which an area
     * was selected.</p>
     */
    public MapComponent getMapComponent() {
        return ((MapComponent) getComponent());
    }


    // ------------------------------------------------------ FacesEvent Methods


    /**
     * <p>Return <code>true</code> if the specified listener is
     * appropriate for this type of event.</p>
     *
     * @param listener {@link FacesListener} to be validated
     */
    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof AreaSelectedListener);
    }


    /**
     * <p>Forward this event to the specified listener.</p>
     *
     * @param listener {@link FacesListener} to receive this event
     */
    public void processListener(FacesListener listener) {
        ((AreaSelectedListener) listener).processAreaSelected(this);
    }


}

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

package javax.faces.component;

import javax.faces.context.FacesContext;
import static javax.faces.component.UIComponentBase.saveAttachedState;
import static javax.faces.component.UIComponentBase.restoreAttachedState;
import javax.el.ValueExpression;
import java.util.*;
import java.io.Serializable;

/**A base implementation for
 * maps which implement the PartialStateHolder and TransientStateHolder interfaces.
 *
 * This can be used as a base-class for all
 * state-holder implementations in components,
 * converters and validators and other implementations
 * of the StateHolder interface.
 */
@SuppressWarnings({"unchecked"})
class ComponentStateHelper implements StateHelper , TransientStateHelper {

    private UIComponent component;
    private boolean isTransient;
    private Map<Serializable, Object> deltaMap;
    private Map<Serializable, Object> defaultMap;
    private Map<Object, Object> transientState;
    
    // ------------------------------------------------------------ Constructors


    public ComponentStateHelper(UIComponent component) {

        this.component = component;
        this.deltaMap = new HashMap<Serializable,Object>();
        this.defaultMap = new HashMap<Serializable,Object>();
        this.transientState = null;
    }


    // ------------------------------------------------ Methods from StateHelper


    /**
     * Put the object in the main-map
     * and/or the delta-map, if necessary.
     *
     * @param key
     * @param value
     * @return the original value in the delta-map, if not present, the old value in the main map
     */
    public Object put(Serializable key, Object value) {

        if(component.initialStateMarked() || value instanceof PartialStateHolder) {
            Object retVal = deltaMap.put(key, value);

            if(retVal==null) {
                return defaultMap.put(key,value);
            }
            else {
                defaultMap.put(key,value);
                return retVal;
            }
        }
        else {
            return defaultMap.put(key,value);
        }
    }


    /**
     * We need to remove from both
     * maps, if we do remove an existing key.
     *
     * @param key
     * @return the removed object in the delta-map. if not present, the removed object from the main map
     */
    public Object remove(Serializable key) {
        if(component.initialStateMarked()) {
            Object retVal = deltaMap.remove(key);

            if(retVal==null) {
                return defaultMap.remove(key);
            }
            else {
                defaultMap.remove(key);
                return retVal;
            }
        }
        else {
            return defaultMap.remove(key);
        }
    }


    /**
     * @see StateHelper#put(java.io.Serializable, String, Object)
     */
    public Object put(Serializable key, String mapKey, Object value) {

        Object ret = null;
        if (component.initialStateMarked()) {
            Map<String,Object> dMap = (Map<String,Object>) deltaMap.get(key);
            if (dMap == null) {
                dMap = new HashMap<String,Object>(5);
                deltaMap.put(key, dMap);
            }
            ret = dMap.put(mapKey, value);

        }
        Map<String,Object> map = (Map<String,Object>) get(key);
        if (map == null) {
            map = new HashMap<String,Object>(8);
            defaultMap.put(key, map);
        }
        if (ret == null) {
            return map.put(mapKey, value);
        } else {
            map.put(mapKey, value);
            return ret;
        }

    }


    /**
     * Get the object from the main-map.
     * As everything is written through
     * from the delta-map to the main-map, this
     * should be enough.
     *
     * @param key
     * @return
     */
    public Object get(Serializable key) {
        return defaultMap.get(key);
    }


    /**
     * @see StateHelper#eval(java.io.Serializable)
     */
    public Object eval(Serializable key) {
        return eval(key, null);
    }


    /**
     * @see StateHelper#eval(java.io.Serializable, Object)
     */
    public Object eval(Serializable key, Object defaultValue) {
        Object retVal = get(key);
        if (retVal == null) {
            ValueExpression ve = component.getValueExpression(key.toString());
            if (ve != null) {
                retVal = ve.getValue(component.getFacesContext().getELContext());
            }

        }

        return ((retVal != null) ? retVal : defaultValue);
    }


    /**
     * @see StateHelper#add(java.io.Serializable, Object)
     */
    public void add(Serializable key, Object value) {

        if (component.initialStateMarked()) {
            List<Object> deltaList = (List<Object>) deltaMap.get(key);
            if (deltaList == null) {
                deltaList = new ArrayList<Object>(4);
                deltaMap.put(key, deltaList);
            }
            deltaList.add(value);
        }
        List<Object> items = (List<Object>) get(key);
        if (items == null) {
            items = new ArrayList<Object>(4);
            defaultMap.put(key, items);
        }
        items.add(value);

    }


    /**
     * @see StateHelper#remove(java.io.Serializable, Object)
     */
    public Object remove(Serializable key, Object valueOrKey) {
        Object source = get(key);
        if (source instanceof Collection) {
            return removeFromList(key, valueOrKey);
        } else if (source instanceof Map) {
            return removeFromMap(key, valueOrKey.toString());
        }
        return null;
    }


    // ------------------------------------------------ Methods from StateHolder


    /**
     * One and only implementation of
     * save-state - makes all other implementations
     * unnecessary.
     *
     * @param context
     * @return the saved state
     */
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if(component.initialStateMarked()) {
            return saveMap(context, deltaMap);
        }
        else {
            return saveMap(context, defaultMap);
        }
    }



    /**
     * One and only implementation of
     * restore state. Makes all other implementations
     * unnecessary.
     *
     * @param context FacesContext
     * @param state the state to be restored.
     */
    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        
        if (!component.initialStateMarked() && !defaultMap.isEmpty())
        {
            defaultMap.clear();
            if(deltaMap != null && !deltaMap.isEmpty())
            {
                deltaMap.clear();
            }
        }
        
        Object[] savedState = (Object[]) state;
        if (savedState[savedState.length - 1] != null) {
            component.initialState = (Boolean) savedState[savedState.length - 1];
        }
        int length = (savedState.length-1)/2;
        for (int i = 0; i < length; i++) {
           Object value = savedState[i * 2 + 1];
            Serializable serializable = (Serializable) savedState[i * 2];
            if (value != null) {
                if (value instanceof Collection) {
                    value = restoreAttachedState(context, value);
                } else if (value instanceof StateHolderSaver) {
                    value = ((StateHolderSaver) value).restore(context);
                } else {
                    value = (value instanceof Serializable
                             ? value
                             : restoreAttachedState(context, value));
                }
            }
            if (value instanceof Map) {
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) value)
                      .entrySet()) {
                    this.put(serializable, entry.getKey(), entry.getValue());
                }
            } else if (value instanceof List) {
                List<Object> list = (List) get(serializable);
                for (Object o : ((List<Object>) value)) {
                    if (list == null || !list.contains(o)) {
                        this.add(serializable, o);
                    }
                }
            } else {
                put(serializable, value);
            }
        }
    }


    /**
     * @see javax.faces.component.StateHolder#isTransient()
     */
    public boolean isTransient() {
        return isTransient;
    }

    
    /**
     * @see StateHolder#setTransient(boolean)
     */
    public void setTransient(boolean newTransientValue) {
        isTransient = newTransientValue;
    }


    // --------------------------------------------------------- Private Methods


    private Object saveMap(FacesContext context, Map<Serializable, Object> map) {

        if (map.isEmpty()) {
            if (!component.initialStateMarked()) {
                // only need to propagate the component's delta status when
                // delta tracking has been disabled.  We're assuming that
                // the VDL will reset the status when the view is reconstructed,
                // so no need to save the state if the saved state is the default.
                return new Object[] { component.initialStateMarked() };
            }
            return null;
        }

        Object[] savedState = new Object[map.size() * 2 + 1];

        int i=0;

        for(Map.Entry<Serializable, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            savedState[i * 2] = entry.getKey();
            if (value instanceof Collection
                  || value instanceof StateHolder
                  || value instanceof Map
                  || !(value instanceof Serializable)) {
                value = saveAttachedState(context,value);
            }
            savedState[i * 2 + 1] = value;
            i++;
        }
        if (!component.initialStateMarked()) {
            savedState[savedState.length - 1] = component.initialStateMarked();
        }
        return savedState;

    }


    private Object removeFromList(Serializable key, Object value) {
        Object ret = null;
        if (component.initialStateMarked() || value instanceof PartialStateHolder) {
            Collection<Object> deltaList = (Collection<Object>) deltaMap.get(key);
            if (deltaList != null) {
                ret = deltaList.remove(value);
                if (deltaList.isEmpty()) {
                    deltaMap.remove(key);
                }
            }
        }
        Collection<Object> list = (Collection<Object>) get(key);
        if (list != null) {
            if (ret == null) {
                ret = list.remove(value);
            } else {
                list.remove(value);
            }
            if (list.isEmpty()) {
                defaultMap.remove(key);
            }
        }
        return ret;
    }


    private Object removeFromMap(Serializable key, String mapKey) {
        Object ret = null;
        if (component.initialStateMarked()) {
            Map<String,Object> dMap = (Map<String,Object>) deltaMap.get(key);
            if (dMap != null) {
                ret = dMap.remove(mapKey);
                if (dMap.isEmpty()) {
                    deltaMap.remove(key);
                }
            }
        }
        Map<String,Object> map = (Map<String,Object>) get(key);
        if (map != null) {
            if (ret == null) {
                ret = map.remove(mapKey);
            } else {
                map.remove(mapKey);

            }
            if (map.isEmpty()) {
                defaultMap.remove(key);
            }
        }
        if (ret != null && !component.initialStateMarked()) {
            deltaMap.remove(key);
        }
        return ret;
    }


    public Object getTransient(Object key)
    {
        return (transientState == null) ? null : transientState.get(key);
    }

    public Object getTransient(Object key, Object defaultValue)
    {
        Object returnValue = (transientState == null) ? null : transientState.get(key);
        if (returnValue != null)
        {
            return returnValue;
        }
        return defaultValue;
    }

    public Object putTransient(Object key, Object value)
    {
        if (transientState == null)
        {
            transientState = new HashMap<Object, Object>();
        }
        return transientState.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public void restoreTransientState(FacesContext context, Object state)
    {
        transientState = (Map<Object, Object>) state;
    }
    
    public Object saveTransientState(FacesContext context)
    {
        return transientState;
    }
}

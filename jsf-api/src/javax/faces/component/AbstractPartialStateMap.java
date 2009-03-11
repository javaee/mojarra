package javax.faces.component;

import javax.faces.context.FacesContext;
import static javax.faces.component.UIComponentBase.saveAttachedState;
import static javax.faces.component.UIComponentBase.restoreAttachedState;
import java.util.*;
import java.io.Serializable;

/**A base implementation for
 * maps which implement the PartialStateHolder interface.
 *
 * This can be used as a base-class for all
 * state-holder implementations in components,
 * converters and validators and other implementations
 * of the StateHolder interface.
 */
public class AbstractPartialStateMap extends HashMap<Serializable, Object> implements PartialStateHolder {

    private boolean storeState=false;
    private boolean isTransient=false;

    private Map<Serializable, Object> deltaMap;

    /**Put the object in the main-map
     * and/or the delta-map, if necessary.
     *
     * @param key
     * @param value
     * @return the original value in the delta-map, if not present, the old value in the main map
     */
    @Override
    public Object put(Serializable key, Object value) {

        if(storeState) {
            Object retVal = deltaMap.put(key, value);

            if(retVal==null) {
                return super.put(key,value);
            }
            else {
                super.put(key,value);
                return retVal;
            }
        }
        else {
            return super.put(key,value);
        }
    }

    /**We need to remove from both
     * maps, if we do remove an existing key.
     *
     * @param key
     * @return the removed object in the delta-map. if not present, the removed object from the main map
     */
    @Override
    public Object remove(Object key) {
        if(storeState) {
            Object retVal = deltaMap.remove(key);

            if(retVal==null) {
                return super.remove(key);
            }
            else {
                super.remove(key);
                return retVal;
            }
        }
        else {
            return super.remove(key);
        }
    }

    /**Get the object from the main-map.
     * As everything is written through
     * from the delta-map to the main-map, this
     * should be enough.
     *
     * @param key
     * @return
     */
    public Object get(Serializable key) {
        return super.get(key);
    }

    /**Get the object from the main-map
     * and allow for a default-value.
     *
     * @param key
     * @param defaultValue value to return if key is not present in main or delta-map
     * @return value - if null, defaultValue
     */
    public Object get(Serializable key, Object defaultValue) {
        Object retVal = super.get(key);
        if(retVal==null) {
            return defaultValue;
        }
        return retVal;
    }

    /**TODO should this be supported?
     *
     * @param map
     */
    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    /**TODO: need to make sure this gets called from
     * somewhere (from the ComponentSystemEvent handling
     * - needs to call through to all implementations
     * of delta-state saved lists and maps).
     */
    public void notifyStoreState() {
        storeState=true;
        deltaMap = new HashMap<Serializable, Object>();
    }

    /**One and only implementation of
     * save-state - makes all other implementations
     * unnecessary.
     *
     * @param context
     * @return the saved state
     */
    public Object saveState(FacesContext context) {
        if(storeState) {
            return saveMap(deltaMap);
        }
        else {
            return saveMap(this);
        }
    }

    private Object saveMap(Map<Serializable, Object> map) {

        FacesContext fc = FacesContext.getCurrentInstance();

        Object[] savedState = new Object[map.size()*2+1];

        int i=1;

        for(Map.Entry<Serializable, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            savedState[i] = entry.getKey();
            savedState[i + 1] = value instanceof Serializable?value:saveAttachedState(fc, entry.getValue());
            i++;
        }
        savedState[0] = storeState;
        return savedState;
    }

    /**One and only implementation of
     * restore state. Makes all other implementations
     * unnecessary.
     *
     * @param context FacesContext
     * @param state the state to be restored.
     */
    public void restoreState(FacesContext context, Object state) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Object[] savedState = (Object[]) state;
        storeState = (Boolean) savedState[0];
        if (storeState) {
            notifyStoreState();
        }
        int length = (savedState.length-1)/2 + 1;
        for (int i = 1; i < length; i++) {
           Object value = savedState[i + 1];
           Serializable serializable = (Serializable) savedState[i];
           Object o = value instanceof Serializable ? value : restoreAttachedState(fc, value);
           put(serializable, o);
        }
    }

    /**TODO decide if this is can just return false
     *
     * @return
     */
    public boolean isTransient() {
        return isTransient;
    }

    /**TODO decide if this should be a NO-OP
     *
     * @param newTransientValue
     */
    public void setTransient(boolean newTransientValue) {
        isTransient = newTransientValue;
    }
}
